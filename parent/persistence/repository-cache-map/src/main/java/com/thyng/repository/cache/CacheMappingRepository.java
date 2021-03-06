package com.thyng.repository.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.thyng.domain.model.Mapping;
import com.thyng.event.EventBus;
import com.thyng.repository.MappingRepository;
import com.thyng.util.Constant;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class CacheMappingRepository implements MappingRepository {
	
	private final Consumer<Mapping> cacheCallback = this::cache;
	private final Consumer<Mapping> evictCallback = this::evict;
	private final Map<String, Mapping> cache = new ConcurrentHashMap<>();
	
	@NonNull private final String entityName;
	@NonNull private final EventBus eventBus;
	@NonNull private final MappingRepository delegate;
	
	@Override
	public void start() throws Exception {
		delegate.start();
		eventBus.subscribe(Constant.createdTopic(entityName), cacheCallback);
		eventBus.subscribe(Constant.createdTopic(entityName), cacheCallback);
		eventBus.subscribe(Constant.createdTopic(entityName), evictCallback);
	}
	
	@Override
	public void stop() throws Exception {
		eventBus.unsubscribe(Constant.createdTopic(entityName), cacheCallback);
		eventBus.unsubscribe(Constant.createdTopic(entityName), cacheCallback);
		eventBus.unsubscribe(Constant.createdTopic(entityName), evictCallback);
		delegate.stop();
		cache.clear();
	}
	
	private void cache(Mapping item) {
		cache.put(item.getId(), item);
	}
	
	private void evict(Mapping item) {
		cache.remove(item.getId());
	}

	private void loadIfEmpty() {
		if(cache.isEmpty()) delegate.findAll().forEach(cacheCallback);
	}
	
	@Override
	public List<Mapping> findAll() {
		loadIfEmpty();
		return Collections.unmodifiableList(new ArrayList<>(cache.values()));
	}
	
	@Override
	public Mapping findById(String id) {
		loadIfEmpty();
		return cache.get(id);
	}

	@Override
	public CompletableFuture<Mapping> save(Mapping mapping) {
		return delegate.save(mapping)
				.thenApply(result -> {
					cacheCallback.accept(result);
					return result;
				});
	}

	@Override
	public CompletableFuture<Collection<Mapping>> saveAll(Collection<Mapping> mappings) {
		return delegate.saveAll(mappings)
				.thenApply(result -> {
					result.forEach(cacheCallback);
					return result;
				});
	}

	@Override
	public CompletableFuture<String> delete(String id) {
		return delegate.delete(id)
				.thenApply(result -> {
					cache.remove(id);
					return result;
				});
	}

	

}
