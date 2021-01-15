package com.thyng.web.handler.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Context;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.web.handler.NotFoundHandler;
import com.thyng.web.handler.rest.crud.DelegateRestCrudHandler;
import com.thyng.web.handler.rest.crud.MultiTenantRestCrudHandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.RequiredArgsConstructor;

@Sharable
@RequiredArgsConstructor
public class RequestRouterHandler extends MessageToMessageDecoder<RestRequest> implements Lifecycle {
	private static final String HANDLER = "thyng-rest-request-handler";

	private static final String CONTEXT_PATH = "";
	
	private final Context context;
	private final ObjectMapper objectMapper;
	private final ChannelHandler notFoundHandler = new NotFoundHandler();
	private final Map<String, ChannelHandler> handlers = new HashMap<>();
	
	@Override
	public void start() throws Exception {
		handlers.put(CONTEXT_PATH, notFoundHandler);
		handlers.put("tenants", new DelegateRestCrudHandler<>(objectMapper, context.getTenantRepository()));
		handlers.put("gateways", new MultiTenantRestCrudHandler<>(objectMapper, context.getGatewayRepository()));
		handlers.put("templates", new MultiTenantRestCrudHandler<>(objectMapper, context.getTemplateRepository()));
		handlers.put("sensors", new MultiTenantRestCrudHandler<>(objectMapper, context.getSensorRepository()));
		handlers.put("actuators", new MultiTenantRestCrudHandler<>(objectMapper, context.getActuatorRepository()));
		handlers.put("things", new MultiTenantRestCrudHandler<>(objectMapper, context.getThingRepository()));
		handlers.put("triggers", new MultiTenantRestCrudHandler<>(objectMapper, context.getTriggerRepository()));
		handlers.put("templates", new MultiTenantRestCrudHandler<>(objectMapper, context.getTemplateRepository()));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, RestRequest request, List<Object> out) throws Exception {
		final String entityName = request.getPathParameters().getOrDefault(0, CONTEXT_PATH);
		ctx.pipeline().addLast(HANDLER, handlers.getOrDefault(entityName, notFoundHandler));
		out.add(request.retain());
	}

}
