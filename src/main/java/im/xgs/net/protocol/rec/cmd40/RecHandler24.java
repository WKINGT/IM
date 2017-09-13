package im.xgs.net.protocol.rec.cmd40;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.chat.FileMsgReq;
import im.xgs.net.protocol.entity.resp.chat.FileMsgResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Date;

import com.alibaba.fastjson.JSON;
/**
 * 文件消息
 * @author TianW
 *
 */
public class RecHandler24 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		String version = Protocol.VERSION;
		short cmd = Protocol.CHATC;
		short opera = Protocol.Chat.FILE;
		
		FileMsgResp fileMsgResp = new FileMsgResp();
		FileMsgReq entity = null;
		try {
			entity = JSON.parseObject(msg, FileMsgReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String fromId = cm.getUserId(channel);
			String toId = entity.getTo();
			MsgToType toType = MsgToType.get(entity.getToType());
			String fileId = entity.getFileId();
			int size = entity.getSize();
			String ext = entity.getExt();
			String title = entity.getTitle();
			
			//上传群文件
			if(toType.equals(MsgToType.TOGROUP)){
				groupService.uploadFile(fileId, toId, fromId);
			}
			
			fileMsgResp.setCode(0);
			fileMsgResp.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			fileMsgResp.setExt(ext);
			fileMsgResp.setFileId(fileId);
			fileMsgResp.setSize(size);
			fileMsgResp.setTitle(title);
			fileMsgResp.setFromUserId(fromId);
			fileMsgResp.setToType(entity.getToType());
			fileMsgResp.setToId(toId);
			fileMsgResp.setCmd(cmd);
			fileMsgResp.setOpera(opera);
			
			String msgBody = JSON.toJSONString(fileMsgResp);
			
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, fileMsgResp);
			//将消息发给其他的自己的客户端
			SendMsg.toSelf(fromId, rMsg.copy(), channel);
			//将消息发给对方
			new Thread(new Sync(toType, fromId, toId, rMsg, msgBody)).start();
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.sendFile");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, fileMsgResp);
		
		return rMsg;
	}
	private class Sync implements Runnable {
		private MsgToType toType;
		private String fromId;
		private String toId;
		ByteBuf rMsg;
		String msgBody;
		public Sync(MsgToType toType, String fromId, String toId, ByteBuf rMsg, String msgBody){
			this.toType = toType;
			this.fromId = fromId;
			this.toId = toId;
			this.rMsg = rMsg;
			this.msgBody = msgBody;
		}
		public void run() {
			
			switch (toType) {
			case TOUSER:
				SendMsg.toOrgUser(toId, fromId, 0, msgBody, rMsg, true);
				imservice.saveOrgUserMsg(fromId, toId, msgBody);
				break;
			case TOGROUP:
				groupService.saveMsg(toId, fromId, msgBody);
				SendMsg.toGroup(fromId, toId, 1, msgBody, rMsg, true);
			default:
				//如果数据不合理，则丢掉
				break;
			}
		}
		
	}

}