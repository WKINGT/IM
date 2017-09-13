package im.xgs.net.protocol.rec.cmd60;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.system.ViewHistorySysNoticeReq;
import im.xgs.net.protocol.entity.resp.system.ViewHistorySysNoticeResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class RecHandler50 extends RecHandler {
	/**
	 * 查看系统通知
	 */
	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		ViewHistorySysNoticeReq entity = null;
		ViewHistorySysNoticeResp resp = new ViewHistorySysNoticeResp();
		String version = Protocol.VERSION;
		short cmd = Protocol.SYSTEMC;
		short opera = Protocol.System.VIEW_HISTORY_SYS_NOTICE;
		try {
			entity = JSON.parseObject(msg, ViewHistorySysNoticeReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
//			String userId = entity.getUserId();
			String userId = cm.getUserId(channel);
			String searchYm = entity.getSearchYm();
			int pageNumber = entity.getPageNumber();
			int pageSize = entity.getPageSize();
			
			Page<Record> messages = imservice.findSysMsg(userId, searchYm, pageNumber, pageSize);
			
			resp.setCode(0);
			resp.setMessages(messages);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.error.viewHistorySysNotice");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, resp);
		return rMsg;
	}

}
