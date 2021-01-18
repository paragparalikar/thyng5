package com.thyng.dynamo.mapper;

import java.util.Collections;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class CounterMapper implements Mapper<Long, Map<String, AttributeValue>> {

	@Override
	public Long map(Map<String, AttributeValue> attributes) {
		if(null == attributes) return 0l;
		if(attributes.isEmpty()) return 0l;
		if(null == attributes.get("val")) return 0l;
		if(null == attributes.get("val").n()) return 0l; 
		return Long.parseLong(attributes.get("val").n());
	}

	@Override
	public Map<String, AttributeValue> unmap(Long count) {
		if(null == count) return Collections.emptyMap();
		return null == count ? Collections.emptyMap() : 
			Collections.singletonMap("val", AttributeValue.builder().n(count.toString()).build());
	}

}
