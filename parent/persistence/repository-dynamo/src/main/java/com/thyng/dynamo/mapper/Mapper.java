package com.thyng.dynamo.mapper;

import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public interface Mapper<T> {
	
	T map(Map<String, AttributeValue> attributes);
	
	Map<String, AttributeValue> unmap(T entity);

}
