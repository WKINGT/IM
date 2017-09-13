package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.exception.ImException;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.channel.Channel;
/**
 * 更改设定
 * @author TianW
 *
 */
public class RecHandler33 extends RecHandler {
	//TODO 更改设定的请求响应实体
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		// TODO 更改设定
		return null;
	}

}
