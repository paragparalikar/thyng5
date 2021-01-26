package com.thyng.repository.event;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;
import com.thyng.repository.NameableRepository;

import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "nameableRepositoryBuilder")
public class EventPublisherNameableRepository<T extends Identifiable<T> & Nameable> 
	extends EventPublisherRepository<T> implements NameableRepository<T> {
	
	@Override
	public boolean existsByName(String id, String name) {
		final NameableRepository<T> delegate =  (NameableRepository<T>) getDelegate();
		return delegate.existsByName(id, name);
	}

}
