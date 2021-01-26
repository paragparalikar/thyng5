package com.thyng.dynamo.repository;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.IdGenerationRepository;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "idGenerationRepositoryBuilder")
public class DynamoIdGenerationRepository<T extends Identifiable<T> & Nameable> extends DynamoRepository<T> 
			implements IdGenerationRepository<T> {

	@NonNull private final CounterRepository counterRepository;

	@Override
	public String nextId() {
		return Long.toString(counterRepository.addAndGet(getTableName(), 1L), Character.MAX_RADIX);
	}
	
	@Override
	public T save(T entity) {
		if(Strings.isBlank(entity.getId())) entity = entity.withId(nextId());
		return super.save(entity);
	}

}
