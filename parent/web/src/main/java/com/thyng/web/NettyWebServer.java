package com.thyng.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyWebServer {

	public static void main(String[] args) throws Exception {
		final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		final EventLoopGroup workerGroup = new NioEventLoopGroup();
		final ServerInitializer serverInitializer = new ServerInitializer();
		final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);

		try {
			serverInitializer.start();
			new ServerBootstrap()
				.option(ChannelOption.SO_BACKLOG, 1024)
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(loggingHandler)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(serverInitializer)
					.bind(8080).sync().channel().closeFuture()
					.addListener(future -> shutdown(serverInitializer))
					.sync();

		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private static void shutdown(ServerInitializer serverInitializer) {
		try {
			serverInitializer.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
