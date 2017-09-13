package im.xgs.net.core;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerConfig {
	private static boolean isLinux;
	static{
		isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
	}
	
	public static EventLoopGroup newEventLoopGroup(){
		if(isLinux){
			return new EpollEventLoopGroup();
		}
		return new NioEventLoopGroup();
	}
	
	public static Class<? extends ServerSocketChannel> ServerSocketChannelClass(){
		if(isLinux)
			return EpollServerSocketChannel.class;
		return NioServerSocketChannel.class;
	}
}
