package com.thyng.repository;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Lifecycle;

public interface ObjectRepository<T extends Identifiable<T>> extends Lifecycle {

	T get(String id);
	
	void put(T item);
	
}
