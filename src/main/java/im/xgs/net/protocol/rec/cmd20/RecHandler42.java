package im.xgs.net.protocol.rec.cmd20;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Record;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.FindOfflineMsgReq;
import im.xgs.net.protocol.entity.resp.request.FindOfflineMsgResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 获取离线消息
 * @author TianW
 *
 */
public class RecHandler42 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		FindOfflineMsgResp findResp = new FindOfflineMsgResp();
		FindOfflineMsgReq entity = null;
		try {
			entity = JSON.parseObject(msg,FindOfflineMsgReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String userId = cm.getUserId(channel);
			String fromId = entity.getFromId();
			List<Record> offlineMsgs = imservice.findOfflineMsg(userId, fromId, entity.getToType());
			findResp.setCode(0);
			findResp.setOfflineMsgs(offlineMsgs);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.findOfflineMsgs");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.FIND_OFFLINE_MSG;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, findResp);
		return rMsg;
	}
}
