package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Actuator;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ActuatorDynamoRepository extends AbstractDynamoMultiTenantRepository<Actuator> {

	public ActuatorDynamoRepository(DynamoDbAsyncClient client) {
		super(Actuator.CACHE_NAME, client);
	}

	@Override
	protected Map<String, AttributeValue> map(Actuator item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		return map;
	}

	@Override
	protected Actuator map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Actuator item = new Actuator();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		item.setTenantId(attributes.get("tenantId").s());
		return item;
	}

}
