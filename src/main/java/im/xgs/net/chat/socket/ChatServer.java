package im.xgs.net.chat.socket;

import im.xgs.net.chat.AbstractChatServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer extends AbstractChatServer{
	
	public ChatServer(int port){
		super(port);
	}
	
	public void run() throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup,workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChatServerInitializer())
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			
			System.out.println("ChatServer start");
			//bossGroup.register(channel)
			ChannelFuture future = bootstrap.bind(this.port).sync();
			//future.channel().connect(remoteAddress);
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			System.out.println("SimpleChatServer stop");
		}
	}
	
	public static void main(String[] args) throws Exception{
		new ChatServer(8122).run();
	}
}
