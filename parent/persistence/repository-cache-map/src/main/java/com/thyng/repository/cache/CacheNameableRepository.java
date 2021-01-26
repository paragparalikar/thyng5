package com.thyng.repository.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;
import com.thyng.repository.NameableRepository;

import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "nameableRepositoryBuilder")
public class CacheNameableRepository<T extends Identifiable<T> & Nameable> extends CacheRepository<T> implements NameableRepository<T>{
	
	private final Map<String, T> nameCache = new ConcurrentHashMap<>();
	
	@Override
	public boolean existsByName(String id, String name) {
		loadIfEmpty();
		final T entity = nameCache.get(name);
		return null == entity ? false : !Objects.equals(id, entity.getId());
	}
	
	@Override
	protected void cache(T item) {
		super.cache(item);
		nameCache.put(item.getName(), item);
	}
	
	@Override
	protected void evict(T item) {
		super.evict(item);
		nameCache.remove(item.getName());
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		nameCache.clear();
	}

}
