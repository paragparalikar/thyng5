package com.thyng.dynamo.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.thyng.domain.enumeration.Language;
import com.thyng.util.Strings;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.utils.Pair;

public interface DynamoMapper<T> extends Mapper<T, Map<String, AttributeValue>> {

	default Map<String, String> mapNames(Collection<Map<String, AttributeValue>> attributes){
		return attributes.stream()
				.map(map -> Pair.<String, String>of(map.get("id").s(), map.get("name").s()))
				.collect(Collectors.toMap(Pair::left, Pair::right));
	}
	
	default String mapS(AttributeValue attribute) {
		return null == attribute || Strings.isBlank(attribute.s()) ? null : attribute.s();
	}
	
	default Language mapLanguage(AttributeValue languageAttribute) {
		return null == languageAttribute || Strings.isBlank(languageAttribute.s()) ? null : Language.valueOf(languageAttribute.s());
	}
	
	default Set<String> mapSs(AttributeValue attribute){
		return null == attribute || !attribute.hasSs() ? Collections.emptySet() : new HashSet<>(attribute.ss());
	}
	
}
