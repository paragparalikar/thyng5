package com.thyng.dynamo.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.thyng.util.Collectionz;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class AttributeMap implements Map<String, AttributeValue> {

	@NonNull @Delegate private final Map<String, AttributeValue> delegate;
	
	public AttributeMap put(String key, Enum<?> value) {
		if(null != value) delegate.put(key, AttributeValue.builder().s(value.name()).build());
		return this;
	}
	
	public <T extends Enum<T>> T getEnum(String key, Class<T> type) {
		final AttributeValue attribute = get(key);
		return null == attribute || Strings.isBlank(attribute.s()) ? null : Enum.valueOf(type, attribute.s());
	}
	
	public AttributeMap put(String key, Boolean value) {
		if(null != value) delegate.put(key, AttributeValue.builder().bool(value).build());
		return this;
	}
	
	public Boolean getBool(String key) {
		final AttributeValue attribute = get(key);
		return null == attribute ? null : attribute.bool();
	}
	
	public AttributeMap put(String key, String value) {
		if(Strings.isNotBlank(value)) delegate.put(key, AttributeValue.builder().s(value).build());
		return this;
	}
	
	public String getS(String key) {
		final AttributeValue attribute = get(key);
		return null == attribute ? null : attribute.s();
	}
	
	public AttributeMap put(String key, Number value) {
		if(null != value) delegate.put(key, AttributeValue.builder().n(value.toString()).build());
		return this;
	}
	
	public Long getLong(String key){
		final AttributeValue attribute = get(key);
		return null == attribute || Strings.isBlank(attribute.n()) ? null : Long.parseLong(attribute.n());
	}

	public AttributeMap put(String key, Collection<String> value) {
		if(Collectionz.isNotNullOrEmpty(value)) delegate.put(key, AttributeValue.builder().ss(value).build());
		return this;
	}
	
	public List<String> getSs(String key){
		final AttributeValue attribute = get(key);
		return null == attribute || null == attribute.ss() ? Collections.emptyList() : attribute.ss(); 
	}
	
	public AttributeMap put(String key, Map<String, AttributeValue> value) {
		if(Collectionz.isNotNullOrEmpty(value)) delegate.put(key, AttributeValue.builder().m(value).build());
		return this;
	}
	
	public Map<String, AttributeValue> getM(String key){
		final AttributeValue attribute = get(key);
		return null == attribute ? null : attribute.m();
	}
}
