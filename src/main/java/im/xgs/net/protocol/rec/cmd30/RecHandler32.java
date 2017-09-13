package im.xgs.net.protocol.rec.cmd30;

import im.xgs.net.exception.ImException;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.channel.Channel;
/**
 * 验证消息
 * @author TianW
 *
 */
public class RecHandler32 extends RecHandler {
	//TODO 验证消息的请求和响应实体
	
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		// TODO 验证消息
		return null;
	}

}
