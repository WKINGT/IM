package im.xgs.net.protocol.rec.cmd20;

import im.xgs.net.exception.ImException;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.channel.Channel;
/**
 * 待办
 * @author TianW
 *
 */
public class RecHandler40 extends RecHandler {
	
	//TODO 待办的请求响应实体
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		// TODO 待办
		return null;
	}

}
