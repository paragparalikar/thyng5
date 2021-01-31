package com.thyng.dynamo.repository;

import java.util.Collections;
import java.util.Map;

import com.thyng.domain.intf.Identifiable;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.ObjectRepository;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@Builder
@RequiredArgsConstructor
public class DynamoObjectRepository<T extends Identifiable<T>> implements ObjectRepository<T> {

	@NonNull private final String tableName;
	@NonNull private final DynamoDbAsyncClient client;
	@NonNull private final Mapper<T, Map<String, AttributeValue>> mapper;
	
	@Override
	public T get(String id) {
		final GetItemResponse response = client.getItem(GetItemRequest.builder()
				.key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
				.consistentRead(true)
				.tableName(tableName)
				.build()).join();
		return mapper.map(response.item());
	}

	@Override
	public void put(T item) {
		client.putItem(PutItemRequest.builder()
				.tableName(tableName)
				.item(mapper.unmap(item))
				.build());
	}

}
