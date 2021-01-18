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
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("templateId", AttributeValue.builder().s(item.getTemplateId()).build());
		map.put("inactivityPeriod", AttributeValue.builder().n(String.valueOf(item.getInactivityPeriod())).build());
		map.put("attributes", AttributeValue.builder().ss(attributesMapper.unmap(item.getAttributes())).build());
		return map;
	}

	@Override
	public Thing map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Thing thing = new Thing();
		thing.setId(attributes.get("id").s());
		thing.setName(attributes.get("name").s());
		if(attributes.containsKey("tenantId")) thing.setTenantId(attributes.get("tenantId").s());
		if(attributes.containsKey("templateId")) thing.setTemplateId(attributes.get("templateId").s());
		if(attributes.containsKey("inactivityPeriod")) thing.setInactivityPeriod(Integer.parseInt(attributes.get("inactivityPeriod").n()));
		if(attributes.containsKey("attributes")) thing.setAttributes(attributesMapper.map(attributes.get("attributes").ss()));
		return thing;
	}

	
	
}
