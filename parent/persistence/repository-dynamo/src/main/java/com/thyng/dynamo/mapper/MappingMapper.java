package com.thyng.dynamo.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.thyng.domain.model.Mapping;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class MappingMapper implements Mapper<Mapping, Map<String, AttributeValue>>{

	@Override
	public Mapping map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return Mapping.builder()
				.id(map.getS("id"))
				.values(Collections.unmodifiableSet(new HashSet<>(map.getSs("vals"))))
				.build();
	}

	@Override
	public Map<String, AttributeValue> unmap(Mapping entity) {
		return null == entity ? null : new AttributeMap(new HashMap<>())
				.put("id", entity.getId())
				.put("vals", entity.getValues());
	}

}
