package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.Sensor;
import com.thyng.repository.CounterRepository;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class SensorDynamoRepository extends AbstractDynamoMultiTenantRepository<Sensor> {

	public SensorDynamoRepository(DynamoDbAsyncClient client, CounterRepository counterRepository) {
		super(Sensor.CACHE_NAME, client, counterRepository);
	}

	@Override
	protected Map<String, AttributeValue> map(Sensor item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("unit", AttributeValue.builder().s(item.getUnit()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		return map;
	}

	@Override
	protected Sensor map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Sensor item = new Sensor();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		item.setUnit(attributes.get("unit").s());
		item.setTenantId(attributes.get("tenantId").s());
		return item;
	}

}
