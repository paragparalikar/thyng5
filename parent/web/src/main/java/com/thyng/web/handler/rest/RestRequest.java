package com.thyng.web.handler.rest;

import java.util.HashMap;
import java.util.Map;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RestRequest extends AbstractReferenceCounted {

	private String body;
	private final HttpMethod method;
	private final Map<Integer, String> pathParameters = new HashMap<>();
	private final Map<String, String> queryParameters = new HashMap<>();
	
	@Override
	public ReferenceCounted touch(Object hint) {
		return this;
	}
	@Override
	protected void deallocate() {
		body = null;
		pathParameters.clear();
		queryParameters.clear();
	}

}
