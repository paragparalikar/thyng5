package com.thyng.domain.intf;

import java.util.List;

public interface Crud<T extends Identifiable<T>> extends Lifecycle {
	
	List<T> findAll();
	
	T save(T entity);
	
	T findById(String id);
	
	T deleteById(String id);

}
