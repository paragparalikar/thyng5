package com.thyng.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CacheRepository<T extends Identifiable<ID> & Nameable, ID> implements Repository<T, ID>{
	
	@Getter
	protected final Map<ID, T> cache;
	protected final Repository<T, ID> delegate;
	
	public void initialize() {
		delegate.findAll().forEach(item -> cache.put(item.getId(), item));
	}

	@Override
	public long count() {
		return cache.size();
	}

	@Override
	public T getOne(ID id) {
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
		final T saved = delegate.save(item);
		cache.put(saved.getId(), saved);
		return saved;
	}
	
	@Override
	public boolean existsByName(ID id, String name) {
		final String trimmedName = name.trim();
		return cache.values().stream()
				.filter(entity -> trimmedName.equalsIgnoreCase(entity.getName().trim()))
				.map(Identifiable::getId)
				.filter(Predicate.isEqual(id).negate())
				.findFirst()
				.isPresent();
	}

}
