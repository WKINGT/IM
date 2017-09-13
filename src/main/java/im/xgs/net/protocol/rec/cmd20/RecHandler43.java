package im.xgs.net.protocol.rec.cmd20;

import com.alibaba.fastjson.JSON;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.DeleteOfflineMsgReq;
import im.xgs.net.protocol.entity.resp.request.DeleteOfflineMsgResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 删除离线消息
 * @author TianW
 *
 */
public class RecHandler43 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		DeleteOfflineMsgReq entity = null;
		DeleteOfflineMsgResp deleteResp = new DeleteOfflineMsgResp();
		try {
			entity = JSON.parseObject(msg, DeleteOfflineMsgReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String fromId = entity.getFromId();
			imservice.deleteOfflineMsg(fromId);
			deleteResp.setCode(0);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.deleteOfflineMsgs");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.DELETE_OFFLINE_MSG;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, deleteResp);
		return rMsg;
	}

}
