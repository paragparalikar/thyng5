package com.thyng;

import java.util.List;

public interface Crud<T, ID> {
	
	List<T> findAll();
	
	T save(T entity);
	
	T findById(ID id);
	
	T deleteById(ID id);
	
	boolean existsByName(ID id, String name);
	
}
