package im.xgs.net.protocol.rec.cmd20;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.resp.request.GroupListResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
/**
 * 请求群组列表
 * 客户端登录后，只需请求一次群列表，根据收到的消息，更新列表
 * @author TianW
 *
 */
public class RecHandler20 extends RecHandler {	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		GroupListResp gL = null;
		try {
			String userId = cm.getUserId(channel);
			List<Record> groups= groupService.findGroups(userId);
			gL = new GroupListResp();
			gL.setCode(0);
			gL.setGroups(groups);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.findGroupLists");
			throw new ImException(code, prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.GROUP_LIST;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, gL);
		return rMsg;
	}

}
