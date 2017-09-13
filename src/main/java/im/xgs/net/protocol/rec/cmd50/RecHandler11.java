package im.xgs.net.protocol.rec.cmd50;

import com.alibaba.fastjson.JSON;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.updateData.UpdateSignReq;
import im.xgs.net.protocol.entity.resp.updateData.UpdateSignResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 更改个人签名
 * @author TianW
 *
 */
public class RecHandler11 extends RecHandler {
	
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		String version = Protocol.VERSION;
		short cmd = Protocol.UPDATE_DATAC;
		short opera = Protocol.UpdateData.STATE_MSG;
		
		UpdateSignResp updateSignResp = new UpdateSignResp();
		UpdateSignReq entity = null;
		try {
			entity = JSON.parseObject(msg, UpdateSignReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String newSign = entity.getNewSign();
			String userId = cm.getUserId(channel);
			
			imservice.updateSign(userId, newSign);
			
			updateSignResp.setCode(0);
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.updateSign");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, updateSignResp);
		
		return rMsg;
	}

}
