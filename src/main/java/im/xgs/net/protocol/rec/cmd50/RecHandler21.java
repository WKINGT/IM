package im.xgs.net.protocol.rec.cmd50;

import com.alibaba.fastjson.JSON;
import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.req.updateData.ChangePwdReq;
import im.xgs.net.protocol.entity.resp.updateData.ChangePwdResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
/**
 * 更改密码
 * @author TianW
 *
 */
public class RecHandler21 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		String version = Protocol.VERSION;
		short cmd = Protocol.UPDATE_DATAC;
		short opera = Protocol.UpdateData.CHANGE_PWD;
		
		ChangePwdResp changePwdResp = new ChangePwdResp();
		ChangePwdReq entity = null;
		try {
			entity = JSON.parseObject(msg, ChangePwdReq.class);
		} catch (Exception e) {
			throw new ImException(prop.getInt("error.format"), "消息格式错误");
		}
		try {
			String oldPwd = entity.getOldPwd();
			String newPwd = entity.getNewPwd();
			String userId = cm.getUserId(channel);
			
			imservice.changePwd(userId, oldPwd, newPwd);
			
			changePwdResp.setCode(0);
			
		} catch (Exception e){
			logger.debug(e.getMessage());
			int code = prop.getInt("error.changePwd");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, changePwdResp);
		
		return rMsg;
	}

}
