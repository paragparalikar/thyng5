package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Gateway;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class GatewayDynamoRepository extends AbstractDynamoMultiTenantRepository<Gateway> {

	public GatewayDynamoRepository(DynamoDbAsyncClient client) {
		super(Gateway.CACHE_NAME, client);
	}

	@Override
	protected Map<String, AttributeValue> map(Gateway item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("publicKey", AttributeValue.builder().s(item.getPublicKey()).build());
		map.put("privateKey", AttributeValue.builder().s(item.getPrivateKey()).build());
		return map;
	}

	@Override
	protected Gateway map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Gateway item = new Gateway();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		item.setTenantId(attributes.get("tenantId").s());
		item.setPublicKey(attributes.get("publicKey").s());
		item.setPrivateKey(attributes.get("privateKey").s());
		return item;
	}

}
