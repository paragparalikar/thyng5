package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Sensor;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class SensorMapper implements Mapper<Sensor> {

	@Override
	public Map<String, AttributeValue> unmap(Sensor item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("unit", AttributeValue.builder().s(item.getUnit()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("templateId", AttributeValue.builder().s(item.getTemplateId()).build());
		return map;
	}

	@Override
	public Sensor map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Sensor item = new Sensor();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		item.setUnit(attributes.get("unit").s());
		item.setTenantId(attributes.get("tenantId").s());
		item.setTemplateId(attributes.get("templateId").s());
		return item;
	}

}
