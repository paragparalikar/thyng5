package com.thyng.dynamo.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface Mapper<T, V> {
	
	T map(V attributes);
	
	default Set<T> map(Collection<V> attributes) {
		return attributes.stream().map(this::map).collect(Collectors.toSet());
	}
	
	V unmap(T entity);

	default Set<V> unmap(Collection<T> entities) {
		final Set<V> values = entities.stream().map(this::unmap).collect(Collectors.toSet());
		return values.isEmpty() ? null : values;
	}

}
