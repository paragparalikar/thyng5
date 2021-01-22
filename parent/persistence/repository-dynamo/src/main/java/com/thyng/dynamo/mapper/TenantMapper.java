package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Tenant;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TenantMapper implements Mapper<Tenant, Map<String, AttributeValue>> {

	@Override
	public Map<String, AttributeValue> unmap(Tenant tenant) {
		return null == tenant ? null : new AttributeMap(new HashMap<>())
			.put("id", tenant.getId())
			.put("name", tenant.getName())
			.put("enabled", tenant.getEnabled());
	}

	@Override
	public Tenant map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return Tenant.builder()
				.id(map.getS("id"))
				.name(map.getS("name"))
				.enabled(map.getBool("enabled"))
				.build();
	}
	
}
