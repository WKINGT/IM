package im.xgs.net.protocol.rec.cmd40;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.chat.FindHistoryMsgReq;
import im.xgs.net.protocol.entity.resp.chat.FindHistoryMsgResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * 查看消息历史记录
 * @author TianW
 *
 */
public class RecHandler30 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		String version = Protocol.VERSION;
		short cmd = Protocol.CHATC;
		short opera = Protocol.Chat.FIND_HISTORY_MSG;
		
		FindHistoryMsgResp findHistotyMsgResp = new FindHistoryMsgResp();
		FindHistoryMsgReq entity = null;
		try {
			entity = JSON.parseObject(msg, FindHistoryMsgReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String userId = cm.getUserId(channel);
			String id = entity.getId();
			MsgToType toType = MsgToType.get(entity.getToType());
			int pageNumber = entity.getPageNumber();
			int pageSize = entity.getPageSize();
			String searchYm = entity.getSearchYm();
			
			switch (toType) {
			case TOUSER:
				Page<Record> messages = imservice.findOrgUserMsg(pageNumber, pageSize, userId, id,searchYm);
				findHistotyMsgResp.setCode(0);
				findHistotyMsgResp.setMessages(messages);
				break;
			case TOGROUP:
				Page<Record> messages1 = groupService.findMsg(pageNumber, pageSize, id,searchYm);
				findHistotyMsgResp.setCode(0);
				findHistotyMsgResp.setMessages(messages1);
				break;
			}
			
		} catch(Exception e){
			logger.debug(e.getMessage());
			int code = prop.getInt("error.findHistoryMsg");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, findHistotyMsgResp);
		
		return rMsg;
	}

}
