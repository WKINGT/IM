package im.xgs.net.protocol.rec.cmd10;

import im.xgs.net.channel.MapChannel;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.resp.login.LogoutResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 登出操作
 * @author TianW
 *
 */
public class RecHandler11 extends RecHandler{
	
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		String userId = cm.getUserId(channel);

		String version = Protocol.VERSION;
		short cmd = Protocol.LOGINC;
		short opera = Protocol.Login.LOGOUT;
		
		LogoutResp out = new LogoutResp();
		out.setCode(0);
//		out.setText("您已在" + ChannelType.get( Integer.parseInt(client)) + "端下线");
		out.setUserId(userId);
		out.setClient(client);
		out.setOtherOnlineClient(cm.getSecondPriorityClient(userId, client));
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, out);
		
		ByteBuf rMsg1 = Message.Packing(version, uuid, Protocol.SYSTEMC, Protocol.System.DOWN_LINE_NOTICE, out);
		
		//给其他自己的客户端发送下线消息
		SendMsg.toSelf(userId, rMsg1.copy(), channel);
		int highTer = cm.getHighestPriorityClient(userId, client);//得到用户当前在线的客户端，如果用户不在线，则返回-1
		if(Integer.parseInt(client)>=highTer){//当下线的的客户端的优先级是最高时，给其他好友发送下线通知知
			//异步给好友和群组发送下线消息
			new Thread(new Sync(userId, rMsg1.copy(),channel)).start();
			
		}else{
			MapChannel.instance().destroy(channel);
		}
		
		return rMsg;
	}
	private class Sync implements Runnable{
		private String userId;
		private ByteBuf rMsg;
		private Channel channel;
		public Sync(String userId,ByteBuf rMsg,Channel channel){
			this.userId = userId;
			this.rMsg = rMsg;
			this.channel = channel;
		}
		public void run() {
			//给好友发送下线信息
			SendMsg.toAllOrgUser(userId, 0, null, rMsg.copy(), false);
			//给群组发送下线信息 
//			SendMsg.toAllGroup(userId, 1, null, rMsg.copy(), false);
			
			MapChannel.instance().destroy(channel);
		}
	
		
	}

}
