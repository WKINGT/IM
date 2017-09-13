package im.xgs.net.chat.socket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

public class ImDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {

		int frameLength = (int) msg.getUnsignedInt(0);// 获取头部
		if (msg.readableBytes() < frameLength)// 当ByteBuf没有达到长度时，return null
		{
			return ;
		}
		msg.skipBytes(4);// 舍弃头部
		int index = msg.readerIndex();
		ByteBuf frame = msg.slice(index, frameLength).retain();// 取出自己定义的packet包返回给ChannelRead
//
		msg.readerIndex(frameLength);// 这一步一定要有，不然其实bytebuf的readerIndex没有变，netty会一直从这里开始读取，将readerIndex移动就相当于把前面的数据处理过了废弃掉了。
		out.add(frame);
//		return frame;
		
	}


}
