package im.xgs.net.protocol.rec.cmd20;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.resp.request.CountOfflineMsgResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 获取离线消息数量
 * @author TianW
 *
 */
public class RecHandler41 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		CountOfflineMsgResp countResp = null;
		try {
			String userId = cm.getUserId(channel);
			List<Record> countOfflineMsg = imservice.coutOfflineMsg(userId);
			countResp = new CountOfflineMsgResp();
			countResp.setCode(0);
			countResp.setCountOfflineMsgs(countOfflineMsg);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.countOfflineMsg");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.COUNT_OFFLINE_MSG;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, countResp);
		return rMsg;
	}

}
