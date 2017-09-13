package im.xgs.net.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
 
/**
 * 客户端 channel
 * 
 * @author waylau.com
 * @date 2015-2-26
 */
public class SimpleChatClientHandler extends  SimpleChannelInboundHandler<String> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
		System.out.println(s);
		i=0;
	}
	int i;
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
  	Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
      // 当出现异常就关闭连接
      cause.printStackTrace();
      ctx.close();
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
					ctx.channel().close();
					logger.debug("掉线");
					return;
				}
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
				ctx.channel().writeAndFlush("heartbeat"+i);
			}
			logger.error("{}超时,类型{}",ctx.channel().remoteAddress(),type);
		} else {
			logger.debug(evt.toString());
			super.userEventTriggered(ctx, evt);
		}
		
	}

  public void channelReadComplete(ChannelHandlerContext ctx){
	  logger.debug("channel 读取完成");
  }
}
