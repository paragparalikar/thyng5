package com.thyng.web.handler.rest.crud;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Callback;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.repository.MultiTenantRepository;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;

@Sharable
public class MultiTenantRestCrudHandler<T extends TenantAwareModel> extends RestCrudHandler<T> {
	
	private final MultiTenantRepository<T> repository;

	public MultiTenantRestCrudHandler(
			@NonNull ObjectMapper objectMapper,
			@NonNull MultiTenantRepository<T> repository) {
		super(objectMapper);
		this.repository = repository;
	}

	protected String resolveTenantId(ChannelHandlerContext ctx) {
		return "1";
	}
	
	@Override
	protected void findAll(Callback<List<T>> callback, ChannelHandlerContext ctx) {
		repository.findAll(resolveTenantId(ctx), callback);
	}

	@Override
	protected void findById(String id, Callback<Optional<T>> callback, ChannelHandlerContext ctx) {
		repository.findById(resolveTenantId(ctx), id, callback);
	}

	@Override
	protected void save(T entity, Callback<T> callback, ChannelHandlerContext ctx) {
		repository.save(entity, callback);
	}

	@Override
	protected void deleteById(String id, Callback<T> callback, ChannelHandlerContext ctx) {
		repository.deleteById(resolveTenantId(ctx), id, callback);
	}

}
