package com.thyng.dynamo.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Tenant;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TenantMapper implements Mapper<Tenant, Map<String, AttributeValue>> {

	@Override
	public Map<String, AttributeValue> unmap(Tenant tenant) {
		if(null == tenant) return Collections.emptyMap();
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(tenant.getId()).build());
		map.put("name", AttributeValue.builder().s(tenant.getName()).build());
		map.put("enabled", AttributeValue.builder().bool(tenant.getEnabled()).build());
		return map;
	}

	@Override
	public Tenant map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Tenant tenant = new Tenant();
		tenant.setId(attributes.get("id").s());
		tenant.setName(attributes.get("name").s());
		tenant.setEnabled(attributes.get("enabled").bool());
		return tenant;
	}
	
}
