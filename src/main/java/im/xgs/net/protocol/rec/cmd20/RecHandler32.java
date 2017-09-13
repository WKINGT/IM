package im.xgs.net.protocol.rec.cmd20;

import com.alibaba.fastjson.JSON;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.SaveRecentContacterReq;
import im.xgs.net.protocol.entity.resp.request.SaveRecentContacterResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 添加最近联系人
 * @author TianW
 *
 */
public class RecHandler32 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		SaveRecentContacterResp saveResp = null;
		SaveRecentContacterReq entity = null;
		try {
			entity = JSON.parseObject(msg, SaveRecentContacterReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String userId = entity.getUserId();
			String friendId = entity.getFriendId();
			String groupId = entity.getGroupId();
			String id = imservice.saveRecentContacter(userId, friendId, groupId);
			saveResp = new SaveRecentContacterResp();
			saveResp.setCode(0);
			saveResp.setId(id);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.saveRecentContacter");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.SAVE_RECENT_CONTACT;
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, saveResp);
		return rMsg;
	}

}
