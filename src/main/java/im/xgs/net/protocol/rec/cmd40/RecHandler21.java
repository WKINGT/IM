package im.xgs.net.protocol.rec.cmd40;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.chat.SoundMsgReq;
import im.xgs.net.protocol.entity.resp.chat.SoundMsgResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Date;

import com.alibaba.fastjson.JSON;
/**
 * 语音消息
 * @author TianW
 *
 */
public class RecHandler21 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		String version = Protocol.VERSION;
//		String uuid = UUIDHexGenerator.getId();
		short cmd = Protocol.CHATC;
		short opera = Protocol.Chat.VOICE;
		SoundMsgResp sMR = new SoundMsgResp();
		SoundMsgReq entity = null;
		try {
			entity = JSON.parseObject(msg, SoundMsgReq.class);
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
			sMR.setCode(0);
			sMR.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			sMR.setExt(ext);
			sMR.setFileId(fileId);
			sMR.setSize(size);
			sMR.setTitle(title);
			sMR.setFromUserId(fromId);
			sMR.setToType(entity.getToType());
			sMR.setToId(toId);
			sMR.setCmd(cmd);
			sMR.setOpera(opera);
			
			String msgBody = JSON.toJSONString(sMR);
			
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, sMR);
			//将消息发给其他的自己的客户端
			SendMsg.toSelf(fromId, rMsg.copy(), channel);
			//将消息发给对方
			new Thread(new Sync(toType, fromId, toId, rMsg, msgBody)).start();
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.sendSound");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, sMR);
		
		return rMsg;
	}
	private class Sync implements Runnable {
		private MsgToType toType;
		private String fromId;
		private String toId;
		ByteBuf rMsg;
		String msgBody;
		public Sync(MsgToType toType, String fromId, String toId,ByteBuf rMsg, String msgBody){
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
