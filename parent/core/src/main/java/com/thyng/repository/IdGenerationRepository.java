package com.thyng.repository;

import com.thyng.domain.intf.Identifiable;

public interface IdGenerationRepository<T extends Identifiable<T>> extends Repository<T> {

	String nextId();
	
}
