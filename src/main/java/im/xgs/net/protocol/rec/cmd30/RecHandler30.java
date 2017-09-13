package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.SendMsg;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.PublishNoticeReq;
import im.xgs.net.protocol.entity.resp.group.NoticeNotify;
import im.xgs.net.protocol.entity.resp.group.PublishNoticeResp;
import im.xgs.net.protocol.rec.RecHandler;
import im.xgs.net.util.UUIDHexGenerator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
/**
 * 发布公告
 * @author TianW
 *
 */
public class RecHandler30 extends RecHandler {
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {

		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.PUBLIC_NOTICE;
		
		PublishNoticeResp publishNotice = new PublishNoticeResp();
		PublishNoticeReq entity = null;
		try {
			entity = JSON.parseObject(msg, PublishNoticeReq.class);	
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupId = entity.getGroupId();
			String notice = entity.getNotice();
			String userId = cm.getUserId(channel);
			groupService.publishNotice(groupId, userId, notice);
			publishNotice.setCode(0);
			//异步通知所有的群成员
			new Thread(new Sync(userId,groupId,notice)).start();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.publishNotice");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, publishNotice);
		
		return rMsg;
	}

	private class Sync implements Runnable{
		private String userId;
		private String groupId;
		private String notice;
		public Sync(String userId,String groupId,String notice){
			this.userId = userId;
			this.groupId = groupId;
			this.notice = notice;
		}
		public void run() {
			String version = Protocol.VERSION;
			String uuid = UUIDHexGenerator.getId();
			short cmd = Protocol.GROUPC;
			short opera = Protocol.Group.PUBLIC_NOTICE;
			
			NoticeNotify noticeNotify = new NoticeNotify();
			noticeNotify.setCode(0);
			noticeNotify.setFromUserId(userId);
			noticeNotify.setToType(1);
			noticeNotify.setToId(groupId);
			noticeNotify.setNotice(notice);
			noticeNotify.setCmd(cmd);
			noticeNotify.setOpera(opera);
			
			ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, noticeNotify);
			String msgBody = JSON.toJSONString(noticeNotify);
			//给其他群成员发送公告消息
			SendMsg.toGroup(userId, groupId, 1, msgBody, rMsg, true);
		}
		
	}
}
