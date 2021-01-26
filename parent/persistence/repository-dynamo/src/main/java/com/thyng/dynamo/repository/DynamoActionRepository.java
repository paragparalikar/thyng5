package com.thyng.dynamo.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thyng.domain.model.Action;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.CounterRepository;

import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

public class DynamoActionRepository extends DynamoTenantAwareRepository<Action> {
	
	private final String tableName;
	private final DynamoDbAsyncClient client;
	private final Mapper<Action, Map<String, AttributeValue>> mapper;

	public DynamoActionRepository(
			@NonNull String tableName,
			@NonNull Mapper<Action, Map<String, AttributeValue>> mapper, 
			@NonNull DynamoDbAsyncClient client,
			@NonNull CounterRepository counterRepository) {
		super(tableName, mapper, client, counterRepository);
		this.client = client;
		this.mapper = mapper;
		this.tableName = tableName;
	}
	
	@Override
	public List<Action> findAll(String tenantId) {
		final Map<String, String> attributeNames = new HashMap<>();
		attributeNames.put("#n", "name");
		attributeNames.put("#t", "type");
		return client.query(QueryRequest.builder()
				.tableName(tableName)
				.select(Select.SPECIFIC_ATTRIBUTES)
				.keyConditionExpression("tenantId = :tenantId")
				.projectionExpression("id,#n,#t")
				.expressionAttributeNames(attributeNames)
				.expressionAttributeValues(Collections.singletonMap(":tenantId", AttributeValue.builder().s(tenantId).build()))
				.build()).join().items().stream()
				.map(mapper::map)
				.collect(Collectors.toList());
	}

}
