package im.xgs.net.protocol.rec.cmd20;



import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.GroupInfoReq;
import im.xgs.net.protocol.entity.resp.request.GroupInfoResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Record;
/**
 * 请求群组详细信息
 * 客户端登录后，点击群组时，请求一次群组详细信息
 * @author TianW
 *
 */
public class RecHandler21 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		GroupInfoReq entity = null;
		GroupInfoResp gInfo = null;
		try {
			entity = JSON.parseObject(msg, GroupInfoReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String groupId = entity.getGroupId();
			Record groupInfo = groupService.getGroupInfo(groupId);
			gInfo = new GroupInfoResp();
			gInfo.setCode(0);
			gInfo.setGroupInfo(groupInfo);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.getGroupInfo");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.GROUP_INFO;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, gInfo);
		
		return rMsg;
	}
}
