package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.RemoveNoticeReq;
import im.xgs.net.protocol.entity.resp.group.RemoveNoticeResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
/**
 * 删除公告
 * @author TianW
 *
 */
public class RecHandler31 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		RemoveNoticeResp removeNotice = new RemoveNoticeResp();
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.REMOVE_NOTICE;
		RemoveNoticeReq entity = null;
		try {
			entity = JSON.parseObject(msg, RemoveNoticeReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try{
			String noticeId = entity.getGroupNoticeId();
			String groupId = entity.getGroupId();
			//从数据库中删除这条公告
			groupService.deleteNotice(noticeId);
			removeNotice.setCode(0);
			removeNotice.setGroupId(groupId);
		} catch(Exception e){
			logger.debug(e.getMessage());
			int code = prop.getInt("error.removeNotice");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, removeNotice);
		
		return rMsg;
	}

}
