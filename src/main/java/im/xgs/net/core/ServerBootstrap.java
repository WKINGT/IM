package im.xgs.net.core;

import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

public class ServerBootstrap {
	private static boolean isLinux;
	static{
		isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
	}
	
	
	public void bind(int port) throws Exception{
		EventLoopGroup boosGroup = ServerConfig.newEventLoopGroup();
		EventLoopGroup workerGroup = ServerConfig.newEventLoopGroup();
		
		try {
			io.netty.bootstrap.ServerBootstrap b = new io.netty.bootstrap.ServerBootstrap();
			b.group(boosGroup, workerGroup).channel(ServerConfig.ServerSocketChannelClass())
			.option(ChannelOption.SO_BACKLOG, 1024);
//			.childHandler(new )
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(isLinux);
	}
	

	
}
