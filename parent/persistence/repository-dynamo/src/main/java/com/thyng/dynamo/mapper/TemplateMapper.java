package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Template;
import com.thyng.util.Collectionz;

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
		if(Collectionz.isNotNullOrEmpty(item.getSensors())) map.put("sensors", AttributeValue.builder().ss(sensorMapper.unmap(item.getSensors())).build());
		if(Collectionz.isNotNullOrEmpty(item.getActuators())) map.put("actuators", AttributeValue.builder().ss(actuatorMapper.unmap(item.getActuators())).build());
		if(Collectionz.isNotNullOrEmpty(item.getAttributes())) map.put("attributes", AttributeValue.builder().ss(attributesMapper.unmap(item.getAttributes())).build());
		return map;
	}

	@Override
	public Template map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Template template = new Template();
		template.setId(attributes.get("id").s());
		template.setName(attributes.get("name").s());
		if(attributes.containsKey("tenantId")) template.setTenantId(attributes.get("tenantId").s());
		if(attributes.containsKey("inactivityPeriod")) template.setInactivityPeriod(Integer.parseInt(attributes.get("inactivityPeriod").n()));
		if(attributes.containsKey("sensors")) template.setSensors(sensorMapper.map(attributes.get("sensors").ss()));
		if(attributes.containsKey("actuators")) template.setActuators(actuatorMapper.map(attributes.get("actuators").ss()));
		if(attributes.containsKey("attributes")) template.setAttributes(attributesMapper.map(attributes.get("attributes").ss()));
		return template;
	}
}
