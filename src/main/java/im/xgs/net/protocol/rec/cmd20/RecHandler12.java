package im.xgs.net.protocol.rec.cmd20;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.request.UserDetailReq;
import im.xgs.net.protocol.entity.resp.request.UserDetailResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Record;
/**
 * 用户详细信息
 * @author TianW
 *
 */
public class RecHandler12 extends RecHandler {
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		UserDetailReq entity = null;
		UserDetailResp uDR = null;
		try {
			entity = JSON.parseObject(msg, UserDetailReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String friendId = entity.getUserId();
			Record userDetail = friendService.getDetailInfo(friendId);
			uDR = new UserDetailResp();
			uDR.setCode(0);
			uDR.setUserDetail(userDetail);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.getUserDetail");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.USER_DETAIL;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, uDR);
		return rMsg;
	}

}
