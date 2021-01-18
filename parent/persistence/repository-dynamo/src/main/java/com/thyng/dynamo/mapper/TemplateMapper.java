package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Template;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class TemplateMapper implements Mapper<Template, Map<String, AttributeValue>> {
	
	@NonNull private final SensorMapper sensorMapper;
	@NonNull private final ActuatorMapper actuatorMapper;
	@NonNull private final AttributeMapper attributesMapper;

	@Override
	public Map<String, AttributeValue> unmap(Template item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("inactivityPeriod", AttributeValue.builder().n(String.valueOf(item.getInactivityPeriod())).build());
		map.put("sensors", AttributeValue.builder().ss(sensorMapper.unmap(item.getSensors())).build());
		map.put("actuators", AttributeValue.builder().ss(actuatorMapper.unmap(item.getActuators())).build());
		map.put("attributes", AttributeValue.builder().ss(attributesMapper.unmap(item.getAttributes())).build());
		return map;
	}

	@Override
	public Template map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Template template = new Template();
		template.setId(attributes.get("id").s());
		template.setName(attributes.get("name").s());
		template.setTenantId(attributes.get("tenantId").s());
		template.setInactivityPeriod(Integer.parseInt(attributes.get("inactivityPeriod").n()));
		template.setSensors(sensorMapper.map(attributes.get("sensors").ss()));
		template.setActuators(actuatorMapper.map(attributes.get("actuators").ss()));
		template.setAttributes(attributesMapper.map(attributes.get("attributes").ss()));
		return template;
	}
}
