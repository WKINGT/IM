package im.xgs.net.protocol.rec.cmd30;

import com.alibaba.fastjson.JSON;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.group.ChangeIdentityReq;
import im.xgs.net.protocol.entity.resp.group.ChangeIdentityResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 更改群成员身份
 * 只能有一个群主，不可将普通成员的身份变为群主
 * 群主的身份也不可改变
 * @author TianW
 *
 */
public class RecHandler12 extends RecHandler {

	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		ChangeIdentityResp changeIdentityResp = null;
		ChangeIdentityReq entity = null;
		try {
			entity = JSON.parseObject(msg, ChangeIdentityReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String ownerId = entity.getOwnerId();
			String userId = entity.getUserId();
			String identity = entity.getIdentity();
			String groupId = entity.getGroupId();
			groupService.updateIdentity(ownerId, userId, identity, groupId);
			
			changeIdentityResp = new ChangeIdentityResp();
			changeIdentityResp.setCode(0);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.changeIdentity");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.GROUPC;
		short opera = Protocol.Group.CHANGE_IDENTITY;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, changeIdentityResp);
		return rMsg;
	}

}
