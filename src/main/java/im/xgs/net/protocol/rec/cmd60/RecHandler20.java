package im.xgs.net.protocol.rec.cmd60;

import im.xgs.net.exception.ImException;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.channel.Channel;
/**
 * 系统通告
 * @author TianW
 *
 */
public class RecHandler20 extends RecHandler {
	//TODO 系统通知的请求响应实体
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		// TODO 系统通告
		return null;
	}

}
