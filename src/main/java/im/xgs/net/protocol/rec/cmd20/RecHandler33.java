package im.xgs.net.protocol.rec.cmd20;

import com.alibaba.fastjson.JSON;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.DeleteRecentContacterReq;
import im.xgs.net.protocol.entity.resp.request.DeleteRecentContacterResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 删除最近联系人
 * @author TianW
 *
 */
public class RecHandler33 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		DeleteRecentContacterReq entity = null;
		DeleteRecentContacterResp deleteResp = null;
		try {
			entity = JSON.parseObject(msg, DeleteRecentContacterReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String id = entity.getId();
			imservice.deleteRecentContacter(id);
			deleteResp = new DeleteRecentContacterResp();
			deleteResp.setCode(0);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.deleteRecentContacter");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.DELETE_RECENT_CONTACT;
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, deleteResp);
		return rMsg;
	}

}
