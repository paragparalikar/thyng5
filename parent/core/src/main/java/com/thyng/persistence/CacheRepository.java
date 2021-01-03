package com.thyng.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.hazelcast.cp.IAtomicLong;
import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CacheRepository<T extends Identifiable<Integer> & Nameable> implements Repository<T, Integer>{
	
	@Getter
	private final Map<Integer, T> cache;
	private final IAtomicLong idProvider;
	private final Repository<T, Integer> delegate;
	
	public void initialize() {
		delegate.findAll().forEach(item -> {
			final Integer id = item.getId();
			cache.put(id, item);
			idProvider.alter(value -> id < value ? value : id);
		});
	}

	@Override
	public long count() {
		return cache.size();
	}

	@Override
	public T getOne(Integer id) {
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
		if(null == item.getId() || 0 == item.getId()) item.setId((int) idProvider.incrementAndGet());
		final T saved = delegate.save(item);
		cache.put(saved.getId(), saved);
		return saved;
	}
	
	@Override
	public boolean existsByName(Integer id, String name) {
		final String trimmedName = name.trim();
		return cache.values().stream()
				.filter(entity -> trimmedName.equalsIgnoreCase(entity.getName().trim()))
				.map(Identifiable::getId)
				.filter(Predicate.isEqual(id).negate())
				.findFirst()
				.isPresent();
	}

}
