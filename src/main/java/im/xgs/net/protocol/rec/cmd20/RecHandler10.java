package im.xgs.net.protocol.rec.cmd20;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.resp.request.FriendListResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
 * 请求好友列表
 * @author TianW
 *
 */
public class RecHandler10 extends RecHandler {
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		FriendListResp fLR = null;
		try {
			String userId = cm.getUserId(channel);
			List<Record> friends = friendService.findFriends(userId);
			fLR = new FriendListResp();
			fLR.setCode(0);
			fLR.setFriends(friends);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.getFriendList");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.FRIEND_LIST;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, fLR);
		
		return rMsg;
	}

}
