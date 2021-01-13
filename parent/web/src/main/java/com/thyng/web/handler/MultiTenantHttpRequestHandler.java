package com.thyng.web.handler;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.aws.utility.StringUtil;
import com.thyng.domain.intf.Callback;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.repository.MultiTenantRepository;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class MultiTenantHttpRequestHandler<T extends TenantAwareModel> extends SimpleChannelInboundHandler<HttpRequest> {

	private final ObjectMapper objectMapper;
	private final MultiTenantRepository<T> repository;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
		final String[] tokens = msg.uri().split("/");
		final String token = 2 < tokens.length && StringUtil.isNotEmpty(tokens[2]) ? tokens[2] : null;
		System.out.println("subtoken : " + token);
		final HttpMethod method = msg.method();
		
		final String tenantId = "1";
		
		if(GET.equals(method) && null == token) {
			repository.findAll(tenantId, Callback.<List<T>>builder()
					.success(items -> send(OK, items, ctx))
					.failure(throwable -> exceptionCaught(ctx, throwable))
					.build());
		} else if (GET.equals(method) && null != token) {
			repository.findById(tenantId, token, Callback.<Optional<T>>builder()
					.failure(throwable -> exceptionCaught(ctx, throwable))
					.success(optional -> send(optional.isPresent()? OK : NOT_FOUND, optional.orElse(null), ctx))
					.build());
		}
	}
	
	@SneakyThrows
	private void send(HttpResponseStatus status, Object content, ChannelHandlerContext ctx) {
		final ByteBuf buffer = null == content ? Unpooled.buffer(0) : Unpooled.wrappedBuffer(objectMapper.writeValueAsBytes(content));
		final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, buffer);
		response.headers()
				.set(CONNECTION, KEEP_ALIVE)
				.set(CONTENT_TYPE, APPLICATION_JSON)
				.setInt(CONTENT_LENGTH, response.content().readableBytes());
		ctx.write(response);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		send(INTERNAL_SERVER_ERROR, null, ctx);
		cause.printStackTrace();
		ctx.close();
	}

}
