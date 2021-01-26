package com.thyng.dynamo.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.model.Template;
import com.thyng.util.Constant;
import com.thyng.util.Strings;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class DynamoTemplateRepository extends DynamoTenantAwareRepository<Template> {
	
	@Override
	public Template save(Template entity) {
		entity = entity.withSensors(populateIds(Constant.SENSOR, entity.getSensors()));
		entity = entity.withActuators(populateIds(Constant.ACTUATOR, entity.getActuators()));
		entity = entity.withAttributes(populateIds(Constant.ATTRIBUTE, entity.getAttributes()));
		return super.save(entity);
	}
	
	private <T extends Identifiable<T>> Set<T> populateIds(String name, Collection<T> items) {
		return Collections.unmodifiableSet(items.stream()
			.map(item -> Strings.isBlank(item.getId()) ? item.withId(nextId(name)) : item)
			.collect(Collectors.toSet()));
	}
	
	private String nextId(String name) {
		return Long.toString(getCounterRepository().addAndGet(name, 1L), Character.MAX_RADIX);
	}

}
