package im.xgs.net.protocol.rec.cmd20;


import im.xgs.net.exception.ImException;
import im.xgs.net.msg.Message;
import im.xgs.net.protocol.entity.Protocol;
import im.xgs.net.protocol.rec.RecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
/**
 * 心跳 收到客户端的心跳包后，返回相同的心跳包
 * 如果30秒内收不到心跳包，或channel没有其他的的读写操作
 * 则将当期管道用户下线，更新session，并关闭管道 
 * @author TianW
 *
 */
public class RecHandler88 extends RecHandler {

	@Override
	public Object exec(String uuid, String msg, boolean isWeb, String client,
			Channel channel) throws ImException {
		String version = Protocol.VERSION;
		short cmd = Protocol.REQUESTC;
		short opera = Protocol.Request.HEART_BEAT;
		ByteBuf rMsg = Message.Packing(version, uuid, cmd, opera, "{}");
		return rMsg;
	}

}
