package com.thyng.dynamo.repository;

import java.util.Collections;

import com.thyng.dynamo.mapper.CounterMapper;
import com.thyng.repository.CounterRepository;
import com.thyng.util.Names;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

@RequiredArgsConstructor
public class DynamoCounterRepository implements CounterRepository {

	@NonNull private final DynamoDbClient client;
	private final CounterMapper counterMapper = new CounterMapper();
	
	@Override
	public long get(String name) {
		final GetItemRequest getItemRequest = GetItemRequest.builder()
				.consistentRead(true)
				.tableName(Names.COUNTER)
				.key(Collections.singletonMap("id", AttributeValue.builder().s(name).build()))
				.build();
		final GetItemResponse getItemResponse = client.getItem(getItemRequest);
		return counterMapper.map(getItemResponse.item());
	}

	@Override
	public long addAndGet(String name, Long delta) {
		final UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
			.tableName(Names.COUNTER)
			.key(Collections.singletonMap("id", AttributeValue.builder().s(name).build()))
			.returnValues(ReturnValue.ALL_NEW)
			.updateExpression("ADD val :val")
			.expressionAttributeValues(Collections.singletonMap(":val", AttributeValue.builder().n(delta.toString()).build()))
			.build();
		final UpdateItemResponse updateItemResponse = client.updateItem(updateItemRequest);
		return counterMapper.map(updateItemResponse.attributes());
	}

}
