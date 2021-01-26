package com.thyng.domain.intf;

import java.util.List;

public interface Source<T> {

	List<T> findAll();
	
	T findById(String id);
	
}
