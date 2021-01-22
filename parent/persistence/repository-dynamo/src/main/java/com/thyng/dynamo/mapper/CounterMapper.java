package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class CounterMapper implements Mapper<Long, Map<String, AttributeValue>> {

	@Override
	public Long map(Map<String, AttributeValue> attributes) {
		return new AttributeMap(attributes).getLong("val");
	}

	@Override
	public Map<String, AttributeValue> unmap(Long count) {
		return null == count ? null : new AttributeMap(new HashMap<>()).put("val", count);
	}

}
