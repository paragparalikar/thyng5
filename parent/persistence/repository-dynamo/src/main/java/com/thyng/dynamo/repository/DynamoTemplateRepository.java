package com.thyng.dynamo.repository;

import java.util.Collection;
import java.util.Map;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.model.Template;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.CounterRepository;
import com.thyng.util.Names;
import com.thyng.util.Strings;

import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoTemplateRepository extends DynamoTenantAwareRepository<Template> {
	
	private final CounterRepository counterRepository;

	public DynamoTemplateRepository(@NonNull Mapper<Template, Map<String, AttributeValue>> mapper,
			@NonNull DynamoDbClient client, @NonNull CounterRepository counterRepository) {
		super(Names.TEMPALTE, mapper, client, counterRepository);
		this.counterRepository = counterRepository;
	}
	
	@Override
	public Template save(Template entity) {
		populateIds(Names.SENSOR, entity.getSensors());
		populateIds(Names.ACTUATOR, entity.getActuators());
		populateIds(Names.ATTRIBUTE, entity.getAttributes());
		return super.save(entity);
	}
	
	private void populateIds(String name, Collection<? extends Identifiable<String>> items) {
		items.stream()
			.filter(item -> Strings.isBlank(item.getId()))
			.forEach(item -> item.setId(nextId(name)));
	}
	
	private String nextId(String name) {
		return Long.toString(counterRepository.addAndGet(name, 1L), Character.MAX_RADIX);
	}

}
