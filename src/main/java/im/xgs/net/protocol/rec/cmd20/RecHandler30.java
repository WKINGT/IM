package im.xgs.net.protocol.rec.cmd20;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.SearchReq;
import im.xgs.net.protocol.entity.resp.request.SearchResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Record;
/**
 * 查找好友或群组
 * @author TianW
 *
 */
public class RecHandler30 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		SearchReq entity = null;
		SearchResp SearchEntity = null;
		try {
			entity = JSON.parseObject(msg, SearchReq.class);	
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String name = entity.getName();
//			List<Record> friendsOrGroups = imservice.findFriendOrGroup(name);
			List<Record> friendsOrGroups = imservice.findMemberOrGroup(name);
			SearchEntity = new SearchResp();
			SearchEntity.setCode(0);
			SearchEntity.setFrinedsOrgroups(friendsOrGroups);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.searchFriendGroup");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.SEARCH;
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, SearchEntity);
		
		return rMsg;
	}

}
