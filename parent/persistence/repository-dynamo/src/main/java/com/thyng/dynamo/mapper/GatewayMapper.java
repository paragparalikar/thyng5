package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Gateway;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class GatewayMapper implements Mapper<Gateway, Map<String, AttributeValue>> {

	@Override
	public Map<String, AttributeValue> unmap(Gateway item) {
		return null == item ? null : new AttributeMap(new HashMap<>())
			.put("id", item.getId())
			.put("name", item.getName())
			.put("tenantId", item.getTenantId())
			.put("publicKey", item.getPublicKey())
			.put("privateKey", item.getPrivateKey());
	}

	@Override
	public Gateway map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return Gateway.builder()
				.id(map.getS("id"))
				.name(map.getS("name"))
				.tenantId(map.getS("tenantId"))
				.publicKey(map.getS("publicKey"))
				.privateKey(map.getS("privateKey"))
				.build();
	}

}
