package im.xgs.net.chat.socket;

import im.xgs.net.handler.MsgHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.jfinal.aop.Enhancer;

public class ChatServerHandler extends SimpleChannelInboundHandler<String>{

	private MsgHandler mh = Enhancer.enhance(MsgHandler.class);
	
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
		//XgsHandler.handlerAdded(false, ctx);
		//不做任何处理
	}
	
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
		mh.handlerRemoved(false, ctx);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//		mh.channelRead0(false, ctx, msg);
	}
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		mh.channelActive(false, ctx);
	}
	
	public void channelInactive(ChannelHandlerContext ctx) throws Exception{
		mh.channelInactive(false, ctx);
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx , Throwable cause){
		mh.exceptionCaught(false, ctx, cause);
	}

//	/**
//	 * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
//	 * A closed Channel is automatically removed from the collection,
//	 */
//	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
//        Channel incoming = ctx.channel();
//        
//        // Broadcast a message to multiple Channels
//        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
//        
//        channels.add(ctx.channel());
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
//        Channel incoming = ctx.channel();
//        
//        // Broadcast a message to multiple Channels
//        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
//        
//        // A closed Channel is automatically removed from ChannelGroup,
//        // so there is no need to do "channels.remove(ctx.channel());"
//    }
//    @Override
//	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception { // (4)
//		Channel incoming = ctx.channel();
//		for (Channel channel : channels) {
//            if (channel != incoming){
//                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
//            } else {
//            	channel.writeAndFlush("[you]" + s + "\n");
//            }
//        }
//	}
//  
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
//        Channel incoming = ctx.channel();
//		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"在线");
//	}
//	
//	@Override
//	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
//        Channel incoming = ctx.channel();
//		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
//	}
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
//    	Channel incoming = ctx.channel();
//		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
//        // 当出现异常就关闭连接
//        cause.printStackTrace();
//        ctx.close();
//    }
}
