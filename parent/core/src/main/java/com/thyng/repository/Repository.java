package com.thyng.repository;

import java.util.List;
import java.util.Optional;

import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;

public interface Repository<T, ID> extends Lifecycle {
	
	void count(Callback<Long> callback);
	
	void findAll(Callback<List<T>> callback);
	
	void save(T entity, Callback<T> callback);
	
	void findById(ID id, Callback<Optional<T>> callback);
	
	void deleteById(ID id, Callback<T> callback);
	
	void existsByName(String id, String name, Callback<Boolean> callback);
	
}
