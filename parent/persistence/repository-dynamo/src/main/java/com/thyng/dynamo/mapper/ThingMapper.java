package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Thing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class ThingMapper implements Mapper<Thing> {
	
	@NonNull private final AttributesMapper attributesMapper;

	@Override
	public Map<String, AttributeValue> unmap(Thing item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("inactivityPeriod", AttributeValue.builder().n(String.valueOf(item.getInactivityPeriod())).build());
		map.put("attributes", AttributeValue.builder().m(attributesMapper.unmap(item.getAttributes())).build());
		return map;
	}

	@Override
	public Thing map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Thing thing = new Thing();
		thing.setId(attributes.get("id").s());
		thing.setName(attributes.get("name").s());
		thing.setTenantId(attributes.get("tenantId").s());
		thing.setInactivityPeriod(Integer.parseInt(attributes.get("inactivityPeriod").n()));
		thing.setAttributes(attributesMapper.map(attributes.get("attributes").m()));
		return thing;
	}

	
	
}
