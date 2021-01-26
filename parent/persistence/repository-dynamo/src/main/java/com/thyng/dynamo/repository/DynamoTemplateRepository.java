package com.thyng.dynamo.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.model.Template;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.CounterRepository;
import com.thyng.util.Names;
import com.thyng.util.Strings;

import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoTemplateRepository extends DynamoTenantAwareRepository<Template> {
	
	private final CounterRepository counterRepository;

	public DynamoTemplateRepository(@NonNull Mapper<Template, Map<String, AttributeValue>> mapper,
			@NonNull DynamoDbAsyncClient client, @NonNull CounterRepository counterRepository) {
		super(Names.TEMPALTE, mapper, client, counterRepository);
		this.counterRepository = counterRepository;
	}
	
	@Override
	public Template save(Template entity) {
		entity = entity.withSensors(populateIds(Names.SENSOR, entity.getSensors()));
		entity = entity.withActuators(populateIds(Names.ACTUATOR, entity.getActuators()));
		entity = entity.withAttributes(populateIds(Names.ATTRIBUTE, entity.getAttributes()));
		return super.save(entity);
	}
	
	private <T extends Identifiable<T, String>> Set<T> populateIds(String name, Collection<T> items) {
		return Collections.unmodifiableSet(items.stream()
			.map(item -> Strings.isBlank(item.getId()) ? item.withId(nextId(name)) : item)
			.collect(Collectors.toSet()));
	}
	
	private String nextId(String name) {
		return Long.toString(counterRepository.addAndGet(name, 1L), Character.MAX_RADIX);
	}

}
