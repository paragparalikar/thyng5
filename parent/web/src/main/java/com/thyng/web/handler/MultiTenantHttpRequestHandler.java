package com.thyng.web.handler;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.aws.utility.StringUtil;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.repository.MultiTenantRepository;
import com.thyng.web.HttpCallback;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class MultiTenantHttpRequestHandler<T extends TenantAwareModel> extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final ObjectMapper objectMapper;
	private final MultiTenantRepository<T> repository;
	
	public MultiTenantHttpRequestHandler(ObjectMapper objectMapper, MultiTenantRepository<T> repository) {
		super(true);
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		final String[] tokens = msg.uri().split("/");
		final String token = 2 < tokens.length && StringUtil.isNotEmpty(tokens[2]) ? tokens[2] : null;
		final HttpMethod method = msg.method();

		final String tenantId = "1";
		
		if(GET.equals(method) && null == token) {
			repository.findAll(tenantId, HttpCallback.<List<T>>builder().ctx(ctx).objectMapper(objectMapper).build());
		} else if (GET.equals(method) && null != token) {
			repository.findById(tenantId, token, HttpCallback.<Optional<T>>builder().ctx(ctx).objectMapper(objectMapper).build());
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.write(new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
		ctx.close();
		log.error("Failed to process HTTP request", cause);
	}

}
