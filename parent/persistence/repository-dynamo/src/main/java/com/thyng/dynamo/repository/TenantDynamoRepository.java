package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Tenant;
import com.thyng.repository.CounterRepository;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TenantDynamoRepository extends AbstractDynamoRepository<Tenant> {

	public TenantDynamoRepository(DynamoDbAsyncClient client, CounterRepository counterRepository) {
		super(Tenant.CACHE_NAME, client, counterRepository);
	}

	@Override
	protected Map<String, AttributeValue> map(Tenant tenant) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(tenant.getId()).build());
		map.put("name", AttributeValue.builder().s(tenant.getName()).build());
		map.put("enabled", AttributeValue.builder().bool(tenant.getEnabled()).build());
		return map;
	}

	@Override
	protected Tenant map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Tenant tenant = new Tenant();
		tenant.setId(attributes.get("id").s());
		tenant.setName(attributes.get("name").s());
		tenant.setEnabled(attributes.get("enabled").bool());
		return tenant;
	}
	
	@Override
	protected Map<String, AttributeValue> keyAttributes(String id) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(id).build());
		return map;
	}

}
