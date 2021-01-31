package com.thyng.repository;

import java.util.Collection;

import com.thyng.domain.intf.Crud;
import com.thyng.domain.intf.Identifiable;

public interface Repository<T extends Identifiable<T>> extends Crud<T> {

	<C extends Collection<T>> C saveAll(C entities);
	
	<C extends Collection<T>> C deleteAll(C entities);
	
}
