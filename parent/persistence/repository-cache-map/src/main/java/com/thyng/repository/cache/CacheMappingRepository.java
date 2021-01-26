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
import com.thyng.repository.MappingRepository;
import com.thyng.service.EventService;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class CacheMappingRepository implements MappingRepository {
	
	private final Consumer<Mapping> cacheCallback = this::cache;
	private final Consumer<Mapping> evictCallback = this::evict;
	private final Map<String, Mapping> cache = new ConcurrentHashMap<>();
	
	@NonNull private final EventService eventService;
	@NonNull private final MappingRepository delegate;
	@NonNull private final String createdTopic, updatedTopic, deletedTopic;
	
	@Override
	public void start() throws Exception {
		delegate.start();
		eventService.subscribe(createdTopic, cacheCallback);
		eventService.subscribe(updatedTopic, cacheCallback);
		eventService.subscribe(deletedTopic, evictCallback);
	}
	
	@Override
	public void stop() throws Exception {
		eventService.unsubscribe(createdTopic, cacheCallback);
		eventService.unsubscribe(updatedTopic, cacheCallback);
		eventService.unsubscribe(deletedTopic, evictCallback);
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
