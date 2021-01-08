package com.thyng.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;

import org.springframework.util.StringUtils;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;
import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;
import com.thyng.domain.Names;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheRepository<T extends Identifiable<String> & Nameable> implements Repository<T, String>{

	private IMap<String, T> cache;
	private IAtomicLong idProvider;
	
	@NonNull private final String cacheName;
	@NonNull private final Repository<T, String> delegate;
	@NonNull private final HazelcastInstance hazelcastInstance;
	
	@PostConstruct
	public void initialize() {
		delegate.initialize();
		this.cache = hazelcastInstance.getMap(cacheName);
		this.idProvider = hazelcastInstance.getCPSubsystem().getAtomicLong(Names.idGenerator(cacheName));
		load(item -> cache.put(item.getId(), item));
	}

	protected void load(Consumer<T> consumer) {
		final FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(Names.lock(cacheName));
		if(cache.isEmpty() && lock.tryLock()) {
			try {
				final AtomicLong maxId = new AtomicLong(0);
				delegate.findAll().forEach(consumer.andThen(item -> {
					final long id = Long.parseUnsignedLong(item.getId(), Character.MAX_RADIX);
					maxId.updateAndGet(value -> Math.max(value, id));
				}));
				final long maxIdValue = maxId.get();
				idProvider.alter(value -> Math.max(value, maxIdValue));
			} finally {
				lock.unlock();
				lock.destroy();
			}
		}
	}

	@Override
	public long count() {
		return cache.size();
	}

	@Override
	public T getOne(String id) {
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
		if(!StringUtils.hasText(item.getId()) || "0".equals(item.getId().trim())) {
			item.setId(Long.toUnsignedString(idProvider.incrementAndGet(), Character.MAX_RADIX));
		}
		final T saved = delegate.save(item);
		cache.put(saved.getId(), saved);
		return saved;
	}
	
	@Override
	public boolean existsByName(String id, String name) {
		final String trimmedName = name.trim();
		return cache.values().stream()
				.filter(entity -> trimmedName.equalsIgnoreCase(entity.getName().trim()))
				.map(Identifiable::getId)
				.filter(Predicate.isEqual(id).negate())
				.findFirst()
				.isPresent();
	}

}
