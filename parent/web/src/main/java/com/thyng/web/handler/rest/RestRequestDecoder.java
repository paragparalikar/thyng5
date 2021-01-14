package com.thyng.web.handler.rest;

import java.net.URI;
import java.util.List;

import com.thyng.util.Strings;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;

public class RestRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {
	
	protected void resolvePathParameters(URI uri, RestRequest restRequest) {
		final String[] pathParameters = uri.getPath().split("/");
		for(int index = 1; index < pathParameters.length; index++) {
			restRequest.getPathParameters().put(index - 1, Strings.nullIfBlank(pathParameters[index]));
		}
	}
	
	protected void resolveQueryParameters(URI uri, RestRequest restRequest) {
		if(null != uri.getQuery()) {
			final String[] queryParameters = uri.getQuery().split("&");
			for(int index = 1; index < queryParameters.length; index++) {
				final String[] pair = queryParameters[index].split("=");
				final String value = 0 < pair.length ? Strings.nullIfBlank(pair[1]) : null;
				restRequest.getQueryParameters().put(pair[0], value);
			}
		}
	}
	
	protected void resolvePayload(FullHttpRequest httpRequest, RestRequest restRequest) {
		if(0 < httpRequest.content().readableBytes()) {
			restRequest.setBody(httpRequest.content().toString());
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest httpRequest, List<Object> out) throws Exception {
		final URI uri = new URI(httpRequest.uri());
		final RestRequest restRequest = new RestRequest(httpRequest.method());
		resolvePathParameters(uri, restRequest);
		resolveQueryParameters(uri, restRequest);
		resolvePayload(httpRequest, restRequest);
		out.add(restRequest);
	}

}
