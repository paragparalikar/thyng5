package com.thyng;

import java.util.List;
import java.util.Map;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;

public interface Crud<T extends Identifiable<ID> & Nameable, ID> {
	
	List<T> findAll();
	
	T save(T entity);
	
	T findById(ID id);
	
	T deleteById(ID id);
	
	Map<ID, String> findAllNames();
	
	boolean existsByName(ID id, String name);
	
}
