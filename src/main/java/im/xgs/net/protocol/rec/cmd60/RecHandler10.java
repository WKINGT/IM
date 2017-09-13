package im.xgs.net.protocol.rec.cmd60;

import im.xgs.net.exception.ImException;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.channel.Channel;
/**
 * 上下线通知
 * @author TianW
 *
 */
public class RecHandler10 extends RecHandler {
	//TODO　上下线的请求响应实体
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		// TODO  上下线通知
		return null;
	}

}
