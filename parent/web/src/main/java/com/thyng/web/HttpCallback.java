package com.thyng.web;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Callback;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class HttpCallback<T> extends Callback<T> {

	private final ObjectMapper objectMapper;
	private final ChannelHandlerContext ctx;
	
	@SneakyThrows
	private void send(HttpResponseStatus status, Object content) {
		final ByteBuf buffer = null == content ? Unpooled.buffer(0) : Unpooled.wrappedBuffer(objectMapper.writeValueAsBytes(content));
		final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, buffer);
		response.headers()
				.set(CONNECTION, KEEP_ALIVE)
				.set(CONTENT_TYPE, APPLICATION_JSON)
				.setInt(CONTENT_LENGTH, response.content().readableBytes());
		ctx.write(response);
	}
	
	@Override
	protected void success(T item, Throwable throwable) {
		super.success(item, throwable);
		if(null == throwable) {
			if(Optional.class.isInstance(item)) {
				final Optional<?> optional = Optional.class.cast(item);
				if(optional.isPresent()) {
					send(HttpResponseStatus.OK, optional.get());
				} else {
					send(HttpResponseStatus.NOT_FOUND, null);
				}
			} else {
				send(HttpResponseStatus.OK, item);
			}
		}
	}
	
	@Override
	protected void failure(T item, Throwable throwable) {
		super.failure(item, throwable);
		if(null != throwable) {
			send(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
			ctx.close();
		}
	}
	
	

}
