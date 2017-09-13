package im.xgs.net.chat.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.xgs.net.channel.MapChannel;
import im.xgs.net.handler.MsgHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import com.jfinal.aop.Enhancer;

public class BufHandler extends ChannelInboundHandlerAdapter {

	private MsgHandler mh = Enhancer.enhance(MsgHandler.class);
	
	private static Logger logger = LoggerFactory.getLogger("xgs.net.im.socket.handle");

	private int i = 0;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	i=0;
    	ByteBuf buf = (ByteBuf)msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		System.out.println(new String(req));
		//TODO 业务逻辑处理
    	mh.channelRead0(false, ctx, req);
    	//所有的操作完成后，消息丢弃掉
    	buf.release();
    	/**
		System.out.println(body);
		ByteBuf test = Unpooled.copiedBuffer(body.getBytes());
		ctx.channel().write(test);
		ctx.channel().flush();
		buf.release();
    	 */
    	
    }
    
	
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
		//XgsHandler.handlerAdded(false, ctx);
		//不做任何处理
	}
	
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
		mh.handlerRemoved(false, ctx);
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
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				i++;
				if(i==3){
					MapChannel.instance().destroy(ctx.channel());
					ctx.channel().close();
					logger.error("{}超时,类型{}",ctx.channel().remoteAddress(),type);
					return;
				}
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
			}
			logger.info("{}超时,类型{}",ctx.channel().remoteAddress(),type);
		} else {
			logger.debug(evt.toString());
			super.userEventTriggered(ctx, evt);
		}
	}
}
