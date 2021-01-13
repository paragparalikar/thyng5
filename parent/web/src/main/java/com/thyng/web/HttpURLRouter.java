package com.thyng.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.aws.utility.StringUtil;
import com.thyng.Context;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.repository.MultiTenantRepository;
import com.thyng.web.handler.MultiTenantHttpRequestHandler;
import com.thyng.web.handler.NotFoundHandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequest;
import lombok.RequiredArgsConstructor;

@Sharable
@RequiredArgsConstructor
public class HttpURLRouter extends MessageToMessageDecoder<HttpRequest> implements Lifecycle {

	private final Context context;
	private final ObjectMapper objectMapper;
	private final Map<String, MultiTenantRepository<?>> repositories = new HashMap<>();
	
	@Override
	public void start() throws Exception {
		repositories.put("gateways", context.getGatewayRepository());
		repositories.put("templates", context.getTemplateRepository());
		repositories.put("sensors", context.getSensorRepository());
		repositories.put("actuators", context.getActuatorRepository());
		repositories.put("things", context.getThingRepository());
		repositories.put("triggers", context.getTriggerRepository());
		repositories.put("action", context.getActionRepository());
		repositories.put("templates", context.getTemplateRepository());
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out) throws Exception {
		final String[] tokens = msg.uri().split("/");
		final String token = 1 < tokens.length && StringUtil.isNotEmpty(tokens[1]) ? tokens[1] : null;
		if(null == token) {
			// Home page handler
			ctx.pipeline().addLast(new NotFoundHandler());
		} else {
			final MultiTenantRepository<?> repository = repositories.get(token);
			if(null == repository) {
				ctx.pipeline().addLast(new NotFoundHandler());
			} else {
				ctx.pipeline().addLast(new MultiTenantHttpRequestHandler<>(objectMapper, repository));
			}
		}
		out.add(msg);
	}

}