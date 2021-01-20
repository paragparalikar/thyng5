package com.thyng.dynamo.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface Mapper<T, V> {
	
	T map(V attributes);
	
	default Set<T> map(Collection<V> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		return attributes.stream().map(this::map).collect(Collectors.toSet());
	}
	
	V unmap(T entity);

	default Set<V> unmap(Collection<T> entities) {
		if(null == entities || entities.isEmpty()) return null;
		final Set<V> values = entities.stream().map(this::unmap).collect(Collectors.toSet());
		return values.isEmpty() ? null : values;
	}

}
