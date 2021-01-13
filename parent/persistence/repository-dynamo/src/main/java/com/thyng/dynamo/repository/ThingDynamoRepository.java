package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.thyng.domain.model.Thing;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ThingDynamoRepository extends AbstractDynamoMultiTenantRepository<Thing> {

	public ThingDynamoRepository(DynamoDbAsyncClient client) {
		super(Thing.CACHE_NAME, client);
	}

	@Override
	protected Map<String, AttributeValue> map(Thing item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("inactivityPeriod", AttributeValue.builder().n(String.valueOf(item.getInactivityPeriod())).build());
		final Map<String, AttributeValue> attributes = item.getAttributes().entrySet().stream()
			.collect(Collectors.toMap(Entry::getKey, entry -> AttributeValue.builder().s(entry.getValue()).build()));
		map.put("attributes", AttributeValue.builder().m(attributes).build());
		return map;
	}

	@Override
	protected Thing map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Thing thing = new Thing();
		thing.setId(attributes.get("id").s());
		thing.setName(attributes.get("name").s());
		thing.setTenantId(attributes.get("tenantId").s());
		thing.setInactivityPeriod(Integer.parseInt(attributes.get("inactivityPeriod").n()));
		thing.setAttributes(attributes.get("attributes").m().entrySet().stream()
			.collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().s())));
		return thing;
	}

}
