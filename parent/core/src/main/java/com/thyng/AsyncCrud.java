package com.thyng;

import java.util.List;
import java.util.Optional;

public interface AsyncCrud<T, ID> {
	
	void count(Callback<Long> callback);
	
	void findAll(Callback<List<T>> callback);
	
	void save(T entity, Callback<T> callback);
	
	void findById(ID id, Callback<Optional<T>> callback);
	
	void deleteById(ID id, Callback<T> callback);
	
	void existsByName(String id, String name, Callback<Boolean> callback);

}
