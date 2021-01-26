package com.thyng.repository.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.thyng.domain.intf.Identifiable;
import com.thyng.repository.Repository;
import com.thyng.service.EventService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@RequiredArgsConstructor
public class CacheRepository<T extends Identifiable<T>> implements Repository<T> {

	private final Consumer<T> cacheCallback = this::cache;
	private final Consumer<T> evictCallback = this::evict;
	private final Map<String, T> cache = new ConcurrentHashMap<>();
	
	@NonNull private final Repository<T> delegate;
	@NonNull private final EventService eventService;
	@NonNull private final String createdTopic, updatedTopic, deletedTopic;
	
	@Override
	public void start() throws Exception {
		delegate.start();
		eventService.subscribe(createdTopic, cacheCallback);
		eventService.subscribe(updatedTopic, cacheCallback);
		eventService.subscribe(deletedTopic, evictCallback);
	}
	
	protected void cache(T item) {
		cache.put(item.getId(), item);
		
	}
	
	protected void evict(T item) {
		cache.remove(item.getId());
	}
	
	@Override
	public void stop() throws Exception {
		eventService.unsubscribe(createdTopic, cacheCallback);
		eventService.unsubscribe(updatedTopic, cacheCallback);
		eventService.unsubscribe(deletedTopic, evictCallback);
		delegate.stop();
		cache.clear();
	}
	
	protected void loadIfEmpty() {
		if(cache.isEmpty()) delegate.findAll().forEach(cacheCallback);
	}

	@Override
	public List<T> findAll() {
		loadIfEmpty();
		return Collections.unmodifiableList(new ArrayList<>(cache.values()));
	}

	@Override
	public T save(T entity) {
		final T result = delegate.save(entity);
		cacheCallback.accept(result);
		return result;
	}

	@Override
	public T findById(String id) {
		loadIfEmpty();
		return cache.get(id);
	}

	@Override
	public T deleteById(String id) {
		final T entity = delegate.deleteById(id);
		evictCallback.accept(entity);
		return entity;
	}

}
