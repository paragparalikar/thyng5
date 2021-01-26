package com.thyng.domain.intf;

public interface NameableCrud<T extends Identifiable<T> & Nameable> extends Crud<T>{
	
	boolean existsByName(String id, String name);
	
}
