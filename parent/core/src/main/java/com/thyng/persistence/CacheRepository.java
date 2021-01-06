package com.thyng.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.IMap;
import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;
import com.thyng.domain.Names;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheRepository<T extends Identifiable<Long> & Nameable> implements Repository<T, Long>{

	private IMap<Long, T> cache;
	private FlakeIdGenerator idGenerator;
	
	@NonNull private final String cacheName;
	@NonNull private final Repository<T, Long> delegate;
	@NonNull private final HazelcastInstance hazelcastInstance;
	
	@PostConstruct
	public void initialize() {
		delegate.initialize();
		this.cache = hazelcastInstance.getMap(cacheName);
		this.idGenerator = hazelcastInstance.getFlakeIdGenerator(Names.idGenerator(cacheName));
		load();
	}
	
	protected void load() {
		final FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(Names.lock(cacheName));
		if(cache.isEmpty() && lock.tryLock()) {
			try {
				delegate.findAll().forEach(this::cache);
			} finally {
				lock.unlock();
				lock.destroy();
			}
		}
	}
	
	protected void cache(T item) {
		final Long id = item.getId();
		cache.put(id, item);
	}

	@Override
	public long count() {
		return cache.size();
	}

	@Override
	public T getOne(Long id) {
		return cache.get(id);
	}

	@Override
	public void delete(T item) {
		delegate.delete(item);
		cache.remove(item.getId());
	}

	@Override
	public List<T> findAll() {
		return new ArrayList<>(cache.values());
	}

	@Override
	public T save(T item) {
		if(null == item.getId() || 0 == item.getId()) item.setId(idGenerator.newId());
		final T saved = delegate.save(item);
		cache.put(saved.getId(), saved);
		return saved;
	}
	
	@Override
	public boolean existsByName(Long id, String name) {
		final String trimmedName = name.trim();
		return cache.values().stream()
				.filter(entity -> trimmedName.equalsIgnoreCase(entity.getName().trim()))
				.map(Identifiable::getId)
				.filter(Predicate.isEqual(id).negate())
				.findFirst()
				.isPresent();
	}

}
