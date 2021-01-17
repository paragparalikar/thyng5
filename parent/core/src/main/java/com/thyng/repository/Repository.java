package com.thyng.repository;

import java.util.List;

import com.thyng.Crud;
import com.thyng.domain.intf.Lifecycle;

public interface Repository<T, ID> extends Crud<T, ID>, Lifecycle {
	List<T> findAll();
	T save(T entity);
	T findById(ID id);
	T deleteById(ID id);
	boolean existsByName(ID id, String name);
}
