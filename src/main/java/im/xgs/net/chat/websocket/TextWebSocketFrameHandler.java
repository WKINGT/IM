package im.xgs.net.chat.websocket;


import im.xgs.net.handler.MsgHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import com.jfinal.aop.Enhancer;
/**
 * TextWebSocketFrameHandler 继承自SimpleChannelInboundHandler，
 * 这个类实现了ChannelInboundHandler接口，ChannelInboundHandler 
 * 提供了许多事件处理的接口方法，然后你可以覆盖这些方法。现在仅仅只需要继承 SimpleChannelInboundHandler 
 * 类而不是你自己去实现接口方法。
 */
public class TextWebSocketFrameHandler extends
		SimpleChannelInboundHandler<TextWebSocketFrame> {

	private MsgHandler mh = Enhancer.enhance(MsgHandler.class);
	
	/**
	 * 覆盖了 channelRead0() 事件处理方法。每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel。
	 * 其中如果是 Netty 5.x 版本时，需要把 channelRead0() 重命名为messageReceived()
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		String arg = msg.text();
//		mh.channelRead0(true, ctx, arg);
	}
	/**
	 * 覆盖了 handlerAdded() 事件处理方法。每当从服务端收到新的客户端连接时，
	 * 客户端的 Channel 存入ChannelGroup列表中，并通知列表中的其他客户端 Channel
	 */
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
		//mh.handlerAdded(true, ctx);
		//不做任何处理
	}
	/**
	 * 覆盖了 handlerRemoved() 事件处理方法。每当从服务端收到客户端断开时，
	 * 客户端的 Channel 移除 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
	 */
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
		mh.handlerRemoved(true, ctx);
	}
	/**
	 * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		mh.channelActive(true, ctx);
	}
	/**
	 * 覆盖了 channelInactive() 事件处理方法。服务端监听到客户端不活动
	 */
	public void channelInactive(ChannelHandlerContext ctx) throws Exception{
		mh.channelInactive(true, ctx);
	}
	/**
	 * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，
	 * 即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。
	 * 在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。
	 * 然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
	 */
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		mh.exceptionCaught(true, ctx, cause);
	}
}
