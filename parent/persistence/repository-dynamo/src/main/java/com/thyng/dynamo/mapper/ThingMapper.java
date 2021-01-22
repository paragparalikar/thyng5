package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Thing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class ThingMapper implements Mapper<Thing, Map<String, AttributeValue>> {
	
	@NonNull private final AttributeMapper attributesMapper;

	@Override
	public Map<String, AttributeValue> unmap(Thing item) {
		return null == item ? null : new AttributeMap(new HashMap<>())
			.put("id", item.getId())
			.put("name", item.getName())
			.put("tenantId", item.getTenantId())
			.put("templateId", item.getTemplateId())
			.put("inactivityPeriod", item.getInactivityPeriod())
			.put("attributes", attributesMapper.unmap(item.getAttributes()));
	}

	@Override
	public Thing map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		final Thing thing = Thing.builder()
				.id(map.getS("id"))
				.name(map.getS("name"))
				.tenantId(map.getS("tenantId"))
				.templateId(map.getS("templateId"))
				.inactivityPeriod(map.getLong("inactivityPeriod"))
				.build();
		thing.getAttributes().addAll(attributesMapper.map(map.getSs("attributes")));
		return thing;
	}

	
	
}
