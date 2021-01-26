package com.thyng.repository.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.service.EventService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@RequiredArgsConstructor
public class CacheTenantAwareRepository<T extends TenantAwareModel<T>> implements TenantAwareRepository<T>{

	private final Consumer<T> cacheCallback = this::cache;
	private final Consumer<T> evictCallback = this::evict;
	private final Map<String, T> flatCache = new ConcurrentHashMap<>();
	private final Map<String, Map<String, T>> cache = new ConcurrentHashMap<>();
	private final Map<String, Map<String, T>> nameCache = new ConcurrentHashMap<>();
	
	@NonNull private final EventService eventService;
	@NonNull private final TenantAwareRepository<T> delegate;
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
		cache.values().forEach(Map::clear);
		cache.clear();
		nameCache.values().forEach(Map::clear);
		nameCache.clear();
	}
	
	private void cache(T item) {
		flatCache.put(item.getId(), item);
		cache.computeIfAbsent(item.getTenantId(), id -> new ConcurrentHashMap<>()).put(item.getId(), item);
		nameCache.computeIfAbsent(item.getTenantId(), id -> new ConcurrentHashMap<>()).put(item.getName(), item);
	}
	
	private void evict(T item) {
		flatCache.remove(item.getId());
		cache.computeIfAbsent(item.getTenantId(), id -> new ConcurrentHashMap<>()).remove(item.getId());
		nameCache.computeIfAbsent(item.getTenantId(), id -> new ConcurrentHashMap<>()).remove(item.getName());
	}
	
	private void loadIfEmpty() {
		if(cache.isEmpty()) delegate.findAll().forEach(cacheCallback);
	}
	
	@Override
	public List<T> findAll() {
		loadIfEmpty();
		return Collections.unmodifiableList(cache.values().stream()
				.map(Map::values)
				.flatMap(Collection::stream)
				.collect(Collectors.toList()));
	}

	@Override
	public List<T> findAll(String tenantId) {
		loadIfEmpty();
		return Collections.unmodifiableList(new ArrayList<>(
				cache.getOrDefault(tenantId, Collections.emptyMap()).values()));
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
		return flatCache.get(id);
	}
	
	@Override
	public T findById(String tenantId, String id) {
		loadIfEmpty();
		return cache.getOrDefault(tenantId, Collections.emptyMap()).get(id);
	}

	@Override
	public T deleteById(String tenantId, String id) {
		final T result = delegate.deleteById(tenantId, id);
		evictCallback.accept(result);
		return result;
	}

	@Override
	public boolean existsByName(String tenantId, String id, String name) {
		loadIfEmpty();
		final T entity = nameCache.getOrDefault(tenantId, Collections.emptyMap()).get(name);
		return null == entity ? false : !Objects.equals(id, entity.getId());
	}

}
