package com.thyng.dynamo.mapper;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class AttributesMapper implements Mapper<Map<String, String>> {

	@Override
	public Map<String, String> map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) Collections.emptyMap();
		return attributes.entrySet().stream().collect(Collectors.toMap(
				Entry::getKey, entry -> entry.getValue().s()));
	}

	@Override
	public Map<String, AttributeValue> unmap(Map<String, String> entity) {
		if(null == entity || entity.isEmpty()) Collections.emptyMap();
		return entity.entrySet().stream().collect(Collectors.toMap(
				Entry::getKey, 
				entry -> AttributeValue.builder().s(entry.getValue()).build()));
	}

	
}
	