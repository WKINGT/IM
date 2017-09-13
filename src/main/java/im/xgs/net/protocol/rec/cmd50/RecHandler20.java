package im.xgs.net.protocol.rec.cmd50;

import im.xgs.net.exception.ImException;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.channel.Channel;
/**
 * 更改个人设置
 * @author TianW
 *
 */
public class RecHandler20 extends RecHandler {
	//TODO 更改个人设置的请求和响应实体
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		// TODO 更改个人设置
		return null;
	}

}
