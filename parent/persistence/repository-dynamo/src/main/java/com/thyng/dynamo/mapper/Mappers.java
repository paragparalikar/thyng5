package com.thyng.dynamo.mapper;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.utils.Pair;

@UtilityClass
public class Mappers {

	public Map<String, String> mapNames(Collection<Map<String, AttributeValue>> attributes){
		return attributes.stream()
				.map(map -> Pair.<String, String>of(map.get("id").s(), map.get("name").s()))
				.collect(Collectors.toMap(Pair::left, Pair::right));
	}
	
}
