package com.thyng.web.handler;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NotFoundHandler extends SimpleChannelInboundHandler<HttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
		send(HttpResponseStatus.NOT_FOUND, ctx);
	}
	
	private void send(HttpResponseStatus status, ChannelHandlerContext ctx) {
		final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
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
		send(HttpResponseStatus.INTERNAL_SERVER_ERROR, ctx);
		cause.printStackTrace();
		ctx.close();
	}

}
