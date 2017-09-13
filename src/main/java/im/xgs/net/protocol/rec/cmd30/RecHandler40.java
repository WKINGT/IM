package im.xgs.net.protocol.rec.cmd30;


import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.GetNoticeReq;
import im.xgs.net.protocol.entity.resp.group.GetNoticeResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 获取公告
 * @author TianW
 *
 */
public class RecHandler40 extends RecHandler {
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		//获取公告的响应实体
		GetNoticeResp getNoticeReap = new GetNoticeResp();
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.GET_NOTICE;
		GetNoticeReq entity = null;
		try {
			entity = JSON.parseObject(msg, GetNoticeReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try{
			String groupId = entity.getGroupId();
			int pageNumber = entity.getPageNumber();
			int pageSize = entity.getPageSize();
			Page<Record> notices = groupService.findNotice(pageNumber, pageSize, groupId);
			getNoticeReap.setCode(0);
			getNoticeReap.setNotices(notices);
		} catch(Exception e){
			logger.debug(e.getMessage());
			int code = prop.getInt("error.getNotice");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, getNoticeReap);
		return rMsg;
	}

}
