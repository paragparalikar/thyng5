package com.thyng.web.handler.rest.crud;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Callback;
import com.thyng.util.Strings;
import com.thyng.util.Throwables;
import com.thyng.web.HttpCallback;
import com.thyng.web.handler.rest.RestRequest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
@RequiredArgsConstructor
public abstract class RestCrudHandler<T> extends SimpleChannelInboundHandler<RestRequest> {
	
	@NonNull private final ObjectMapper objectMapper;
	
	protected abstract void findAll(Callback<List<T>> callback, ChannelHandlerContext ctx);
	
	protected abstract void findById(String id, Callback<Optional<T>> callback, ChannelHandlerContext ctx);
	
	protected abstract void save(T entity, Callback<T> callback, ChannelHandlerContext ctx);
	
	protected abstract void deleteById(String id, Callback<T> callback, ChannelHandlerContext ctx);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RestRequest request) throws Exception {
		final HttpMethod method = request.getMethod();
		final String id = request.getPathParameters().get(1);
		if(HttpMethod.GET.equals(method) && Strings.isBlank(id)) {
			findAll(HttpCallback.<List<T>>builder().ctx(ctx).objectMapper(objectMapper).build(), ctx);
		} else if(HttpMethod.GET.equals(method) && Strings.isNotBlank(id)) {
			findById(id, HttpCallback.<Optional<T>>builder().ctx(ctx).objectMapper(objectMapper).build(), ctx);
		} else if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method)) {
			if(Strings.isNotBlank(request.getBody())) {
				final T entity = objectMapper.readValue(request.getBody(), new TypeReference<T>() {});
				save(entity, HttpCallback.<T>builder().ctx(ctx).objectMapper(objectMapper).build(), ctx);
			} else {
				error("Rest entity not found in request body", HttpResponseStatus.BAD_REQUEST, ctx);
			}
		} else if (HttpMethod.DELETE.equals(method) && Strings.isNotBlank(id)) {
			deleteById(id, HttpCallback.<T>builder().ctx(ctx).objectMapper(objectMapper).build(), ctx);
		} else {
			error("Rest endpoint not found", HttpResponseStatus.NOT_FOUND, ctx);
		}
	}	
	
	protected void error(String message, HttpResponseStatus status, ChannelHandlerContext ctx) {
		final ByteBuf buffer = Unpooled.wrappedBuffer(message.getBytes());
		ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buffer));
		log.error(message);
		ctx.close();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		error(Throwables.stackTrace(cause), HttpResponseStatus.INTERNAL_SERVER_ERROR, ctx);
	}
	
}
