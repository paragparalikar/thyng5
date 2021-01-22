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
		return null == item ? null : new AttributeMap(new HashMap<>())
			.put("id", item.getId())
			.put("name", item.getName())
			.put("tenantId", item.getTenantId())
			.put("inactivityPeriod", item.getInactivityPeriod())
			.put("sensors", sensorMapper.unmap(item.getSensors()))
			.put("actuators", actuatorMapper.unmap(item.getActuators()))
			.put("attributes", attributesMapper.unmap(item.getAttributes()));
	}

	@Override
	public Template map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		final Template template = Template.builder()
			.id(map.getS("id"))
			.name(map.getS("name"))
			.tenantId(map.getS("tenantId"))
			.inactivityPeriod(map.getLong("inactivityPeriod"))
			.build();
		template.getSensors().addAll(sensorMapper.map(map.getSs("sensors")));
		template.getActuators().addAll(actuatorMapper.map(map.getSs("actuators")));
		template.getAttributes().addAll(attributesMapper.map(map.getSs("attributes")));
		return template;
	}
}
