package im.xgs.net.chat.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
/**
 * 扩展 SimpleChannelInboundHandler 
 * 用于处理 FullHttpRequest信息
 * @author hasee
 *
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String wsUri;
	private static final File INDEX;
	
	static{
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			String path = location.toURI()+"WebsocketChatClient.html";
			path = !path.contains("file:")?path:path.substring(5);
			INDEX = new File(path);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Unable to locate WebsocketChatClient.html", e);
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
			throws Exception {
		if(wsUri.equalsIgnoreCase(request.uri())){
			/**
			 * 如果请求是 WebSocket 升级，
			 * 递增引用计数器（保留）并且将它传递给在 ChannelPipeline 
			 * 中的下个 ChannelInboundHandler
			 */
			ctx.fireChannelRead(request.retain());
		}
		else{
			if(HttpUtil.is100ContinueExpected(request)){
				//处理符合 HTTP 1.1的 "100 Continue" 请求
				send100Continue(ctx);
			}
			//读取默认的 WebsocketChatClient.html 页面
			RandomAccessFile  file = new RandomAccessFile(INDEX, "r");
			
			HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8");
			//判断 keepalive 是否在请求头里面
			boolean keepAlive = HttpUtil.isKeepAlive(request);
			
			if(keepAlive){
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH,file.length());
				response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
			}
			//写 HttpResponse 到客户端
			ctx.write(response);
			/**
			 * 写 index.html 到客户端，
			 * 判断 SslHandler 是否在 ChannelPipeline 来决定是使用 DefaultFileRegion 
			 * 还是ChunkedNioFile
			 */
			if(ctx.pipeline().get(SslHandler.class) == null){
				ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
			}else{
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}
			//写并刷新 LastHttpContent 到客户端，标记响应完成
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if(!keepAlive){
				//如果 keepalive 没有要求，当写完成时，关闭 Channel
				future.addListener(ChannelFutureListener.CLOSE);
			}
			file.close();
		}
		
	}

	public HttpRequestHandler(String wsUri){
		this.wsUri = wsUri;
	}
	
	private static void send100Continue(ChannelHandlerContext ctx){
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		Channel incomming = ctx.channel();
		System.out.println("Client:"+incomming.remoteAddress()+"异常");
		cause.printStackTrace();
		ctx.close();
	}
}
