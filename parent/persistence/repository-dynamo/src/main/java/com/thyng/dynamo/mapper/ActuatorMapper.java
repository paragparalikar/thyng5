package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Actuator;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ActuatorMapper implements Mapper<Actuator> {

	@Override
	public Map<String, AttributeValue> unmap(Actuator item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		return map;
	}

	@Override
	public Actuator map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Actuator item = new Actuator();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		item.setTenantId(attributes.get("tenantId").s());
		return item;
	}
}
