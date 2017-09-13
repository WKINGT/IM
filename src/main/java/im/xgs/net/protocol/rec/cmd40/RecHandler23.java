package im.xgs.net.protocol.rec.cmd40;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.chat.ImageMsgReq;
import im.xgs.net.protocol.entity.resp.chat.ImageMsgResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Date;

import com.alibaba.fastjson.JSON;
/**
 * 截图消息
 * @author TianW
 *
 */
public class RecHandler23 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		String version = Protocol.VERSION;
//		String uuid = UUIDHexGenerator.getId();
		short cmd = Protocol.CHATC;
		short opera = Protocol.Chat.SCREENSHOT;
		
		ImageMsgResp imageMsgResp = new ImageMsgResp();
		ImageMsgReq entity = null;
		try {
			entity = JSON.parseObject(msg, ImageMsgReq.class);
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
			
			imageMsgResp.setCode(0);
			imageMsgResp.setTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
			imageMsgResp.setExt(ext);
			imageMsgResp.setFileId(fileId);
			imageMsgResp.setSize(size);
			imageMsgResp.setTitle(title);
			imageMsgResp.setFromUserId(fromId);
			imageMsgResp.setToType(entity.getToType());
			imageMsgResp.setToId(toId);
			imageMsgResp.setCmd(cmd);
			imageMsgResp.setOpera(opera);
			
			String msgBody = JSON.toJSONString(imageMsgResp);
			
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, imageMsgResp);
			//将消息发给其他的自己的客户端
			SendMsg.toSelf(fromId, rMsg.copy(), channel);
			//将消息发给对方
			new Thread(new Sync(toType, fromId, toId, rMsg, msgBody)).start();
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.sendScreenShot");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, imageMsgResp);
		
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
