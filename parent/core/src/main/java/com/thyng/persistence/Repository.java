package com.thyng.persistence;

import java.util.List;

import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;

public interface Repository<T extends Identifiable<ID> & Nameable, ID> {
	
	void initialize();

	long count();
	
	T getOne(ID id);
	
	void delete(T item);

	List<T> findAll();

	T save(T item);
	
	boolean existsByName(ID id, String name);

}
