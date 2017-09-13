package im.xgs.net.protocol.rec.cmd10;

import im.xgs.net.channel.MapChannel;
import im.xgs.net.channel.OrgUser;
import im.xgs.net.channel.Session;
import im.xgs.net.exception.ImException;
import im.xgs.net.model.SysOrgUser;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.login.LoginReq;
import im.xgs.net.protocol.entity.resp.login.LoginResp;
import im.xgs.net.protocol.entity.resp.system.LoginKillResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.UUIDHexGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Record;
/**
 * 登入操作
 * @author TianW
 *
 */
public class RecHandler10 extends RecHandler {

	
	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		LoginReq entity = null;
		try {
			entity = JSON.parseObject(msg, LoginReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		SysOrgUser user = imservice.login(entity.getLoginAccount(), entity.getLoginPwd());
		if(user == null){
			// 输出错误信息
			throw new ImException(prop.getInt("error.loginuser"), "用户名或密码错误");
		}

		String userId = user.getId();
		Session session = cm.get(userId);
		int highTer = cm.getHighestPriorityClient(userId, client);//得到用户当前在线的客户端，如果用户不在线，则返回-1
		if(session == null){
			session = new Session();
			List<Record> friends = friendService.findFriends(userId);
			List<Record> groups = groupService.findGroups(userId);
			Map<String, String> fmap = session.getFriends();
			for(Record record : friends){
				fmap.put(record.getStr("friendId"), RecHandler.emptyStr);
			}

			Map<String, String> gmap = session.getGroups();
			for(Record record: groups){
				String gid = record.getStr("groupId");
				gmap.put(gid, "");
			}
			session.getInfo().setName(user.getName());
//			session.getInfo().setStateMsg(user.getSign());
		}else{
			Channel ch = session.getTerminal().get(client);
			session.getTerminal().remove(client);
			if(ch!=null){
				MapChannel.instance().remove(ch);
				Object obj = new LoginKillResp().setCode(0).setText("你已经被踢下线！"+channel.remoteAddress());
				ByteBuf tmsg = Message.Packing(Protocol.VERSION, UUIDHexGenerator.getId(), Protocol.SYSTEMC, Protocol.System.KICKED_OFF, obj);
				ch.writeAndFlush(tmsg);
			}
		}
		
		session.getTerminal().put(client, channel);
		
		MapChannel.instance().put(userId, session, channel);
		
		OrgUser.instance().get(userId).setOnline(true);
		
		String version = Protocol.VERSION;
		short cmd = Protocol.LOGINC;
		short opera = Protocol.Login.LOGIN;
		
		LoginResp in = new LoginResp();
		in.setCode(0);
//		in.setText("您已在" + ChannelType.get( Integer.parseInt(client)) + "端上线");
		in.setUserId(userId);
		in.setClient(client);
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, in);
		
		ByteBuf rMsg1 = Message.Packing(version, uuid, Protocol.SYSTEMC, Protocol.System.UP_LINE_NOTICE, in);
		
		// 给其他自己的客户端发送上线消息
		SendMsg.toSelf(userId, rMsg1.copy(), channel);
		
		if(Integer.parseInt(client) > highTer){//上线的客户端的优先级比之前在线客户端优先级高时，通知其他人
			//给好友和群组异步发送上线消息
			new Thread(new Sync(userId,rMsg1.copy())).start();
		}
		return rMsg;
	}
	private class Sync implements Runnable{
		private String userId;
		private ByteBuf rMsg;
		public Sync(String userId,ByteBuf rMsg){
			this.userId = userId;
			this.rMsg = rMsg;
		}
		public void run() {
			//给好友发送上线信息
			SendMsg.toAllOrgUser(userId, 0, null, rMsg.copy(), false);
			//给群组发送上线信息 
//			SendMsg.toAllGroup(userId, 1, null, rMsg.copy(), false);
		}
		
	}
}
