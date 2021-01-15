package com.thyng.web;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Context;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Module;
import com.thyng.web.handler.rest.RequestRouterHandler;
import com.thyng.web.handler.rest.RestRequestDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel> implements Lifecycle {
	
	private final Context context = new Context();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final List<Module> modules = new LinkedList<>();
	private final CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().build();
	private final RestRequestDecoder restRequestDecoder = new RestRequestDecoder();
	private final RequestRouterHandler requestRouterHandler = new RequestRouterHandler(context, objectMapper);

	public ServerInitializer() {
		ServiceLoader.load(Module.class).forEach(modules::add);
		modules.sort(Comparator.naturalOrder());
	}
	
	@Override
	public void start() throws Exception {
		for(Module module : modules) {
			module.setContext(context);
			module.start();
		}
		context.start();
		requestRouterHandler.start();
		new DevDataLoader(context).run();
	}
	
	@Override
	public void stop() throws Exception {
		modules.sort(Comparator.reverseOrder());
		for(Module module : modules) {
			module.stop();
		}
		context.stop();
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		channel.pipeline()
			.addLast(new HttpServerCodec())
			.addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
			.addLast(new CorsHandler(corsConfig))
			.addLast(restRequestDecoder)
			.addLast(requestRouterHandler);
	}

}
