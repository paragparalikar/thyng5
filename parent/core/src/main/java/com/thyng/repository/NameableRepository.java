package com.thyng.repository;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;
import com.thyng.domain.intf.NameableCrud;

public interface NameableRepository<T extends Identifiable<T> & Nameable> 
				extends Repository<T>, NameableCrud<T> {

}
