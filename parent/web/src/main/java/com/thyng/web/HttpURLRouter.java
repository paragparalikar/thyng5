package com.thyng.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.aws.utility.StringUtil;
import com.thyng.Context;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.web.handler.MultiTenantHttpRequestHandler;
import com.thyng.web.handler.NotFoundHandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import lombok.RequiredArgsConstructor;

@Sharable
@RequiredArgsConstructor
public class HttpURLRouter extends MessageToMessageDecoder<FullHttpRequest> implements Lifecycle {

	private final Context context;
	private final ObjectMapper objectMapper;
	private final ChannelHandler notFoundHandler = new NotFoundHandler();
	private final Map<String, ChannelHandler> handlers = new HashMap<>();
	
	@Override
	public void start() throws Exception {
		handlers.put("", notFoundHandler);
		handlers.put("gateways", new MultiTenantHttpRequestHandler<>(objectMapper, context.getGatewayRepository()));
		handlers.put("templates", new MultiTenantHttpRequestHandler<>(objectMapper, context.getTemplateRepository()));
		handlers.put("sensors", new MultiTenantHttpRequestHandler<>(objectMapper, context.getSensorRepository()));
		handlers.put("actuators", new MultiTenantHttpRequestHandler<>(objectMapper, context.getActuatorRepository()));
		handlers.put("things", new MultiTenantHttpRequestHandler<>(objectMapper, context.getThingRepository()));
		handlers.put("triggers", new MultiTenantHttpRequestHandler<>(objectMapper, context.getTriggerRepository()));
		handlers.put("action", new MultiTenantHttpRequestHandler<>(objectMapper, context.getActionRepository()));
		handlers.put("templates", new MultiTenantHttpRequestHandler<>(objectMapper, context.getTemplateRepository()));
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
		final String[] tokens = msg.uri().split("/");
		final String token = 1 < tokens.length && StringUtil.isNotEmpty(tokens[1]) ? tokens[1].trim() : "";
		ctx.pipeline().addLast(handlers.getOrDefault(token, notFoundHandler));
		out.add(msg.retain());
	}
	
}
