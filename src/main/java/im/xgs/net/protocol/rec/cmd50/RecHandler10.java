package im.xgs.net.protocol.rec.cmd50;

import im.xgs.net.exception.ImException;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.channel.Channel;
/**
 * 更新资料
 * @author TianW
 *
 */
public class RecHandler10 extends RecHandler {
	//TODO　更新资料的请求响应实体
	@Override
	public Object exec(String uuid,String msg, boolean isWeb, String client, Channel channel)
			throws ImException {
		// TODO 更新资料
		return null;
	}

}
