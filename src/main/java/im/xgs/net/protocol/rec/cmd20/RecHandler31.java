package im.xgs.net.protocol.rec.cmd20;

import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.entity.resp.request.FindRecentContacterResp;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
/**
 * 获取最近联系人
 * @author TianW
 *
 */
public class RecHandler31 extends RecHandler {
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		
		
		FindRecentContacterResp RContacter = null;
		try {
			String userId = cm.getUserId(channel);
			
			List<Record> recentContacters =  imservice.findRecentContacter(userId);
			RContacter = new FindRecentContacterResp();
			RContacter.setCode(0);
			RContacter.setRecentContacters(recentContacters);
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
			int code = prop.getInt("error.getRecentContacter");
			throw new ImException(code,prop.get(code+"",e.getMessage()));
		}
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.FIND_RECENT_CONTACT;
		
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, RContacter);
		return rMsg;
	}

}
