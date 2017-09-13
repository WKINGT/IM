package im.xgs.net.chat.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
/**
 * 扩展 ChannelInitializer
 * @author hasee
 *
 */
public class WebsocketChatServerInitializer extends ChannelInitializer<SocketChannel>  {

	/**
	 * 添加 ChannelHandler　到 ChannelPipeline
	 * initChannel() 方法设置 ChannelPipeline 中所有新注册的 Channel,
	 * 安装所有需要的　 ChannelHandler。
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64*1024));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpRequestHandler("/ws"));
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast(new TextWebSocketFrameHandler());
		
//		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//		pipeline.addLast("decoder",new StringDecoder());
//		pipeline.addLast("encoder",new StringEncoder());
//		pipeline.addLast("handler",new SimpleChatServerHandler());
	}

}
