package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Gateway;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class GatewayMapper implements Mapper<Gateway, Map<String, AttributeValue>> {

	@Override
	public Map<String, AttributeValue> unmap(Gateway item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("publicKey", AttributeValue.builder().s(item.getPublicKey()).build());
		map.put("privateKey", AttributeValue.builder().s(item.getPrivateKey()).build());
		return map;
	}

	@Override
	public Gateway map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Gateway item = new Gateway();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		if(attributes.containsKey("tenantId")) item.setTenantId(attributes.get("tenantId").s());
		if(attributes.containsKey("publicKey")) item.setPublicKey(attributes.get("publicKey").s());
		if(attributes.containsKey("privateKey")) item.setPrivateKey(attributes.get("privateKey").s());
		return item;
	}

}
