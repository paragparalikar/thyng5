package com.thyng.web.handler.rest.crud;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.AsyncCrud;
import com.thyng.Callback;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;

@Sharable
public class DelegateRestCrudHandler<T> extends RestCrudHandler<T> {
	
	private final AsyncCrud<T, String> delegate;

	public DelegateRestCrudHandler(@NonNull ObjectMapper objectMapper, @NonNull AsyncCrud<T, String> delegate) {
		super(objectMapper);
		this.delegate = delegate;
	}

	@Override
	protected void findAll(Callback<List<T>> callback, ChannelHandlerContext ctx) {
		delegate.findAll(callback);
	}

	@Override
	protected void findById(String id, Callback<Optional<T>> callback, ChannelHandlerContext ctx) {
		delegate.findById(id, callback);
	}

	@Override
	protected void save(T entity, Callback<T> callback, ChannelHandlerContext ctx) {
		delegate.save(entity, callback);
	}

	@Override
	protected void deleteById(String id, Callback<T> callback, ChannelHandlerContext ctx) {
		delegate.deleteById(id, callback);
	}

}
