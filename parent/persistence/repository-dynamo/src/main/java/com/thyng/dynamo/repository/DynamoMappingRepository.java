package com.thyng.dynamo.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thyng.Callback;
import com.thyng.domain.model.Mapping;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.MappingRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.Select;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

@RequiredArgsConstructor
public class DynamoMappingRepository implements MappingRepository {
	
	@NonNull private final String tableName;
	@NonNull private final DynamoDbAsyncClient client;
	@NonNull private final Mapper<Mapping, Map<String, AttributeValue>> mapper;

	@Override
	public List<Mapping> findAll() {
		ScanResponse response = null;
		Map<String, AttributeValue> lastEvaluatedKey = null;
		final List<Mapping> mappings = new LinkedList<>();
		do {
			response = client.scan(ScanRequest.builder()
					.tableName(tableName)
					.select(Select.ALL_ATTRIBUTES)
					.build()).join();
			response.items().forEach(item -> mappings.add(mapper.map(item)));
			lastEvaluatedKey = response.lastEvaluatedKey();
		} while(null != response
				&& response.hasLastEvaluatedKey()
				&& null != lastEvaluatedKey
				&& !lastEvaluatedKey.isEmpty());
		return mappings;
	}
	
	@Override
	public void save(Mapping mapping, Callback<Mapping> callback) {
		client.putItem(PutItemRequest.builder()
				.tableName(tableName)
				.item(mapper.unmap(mapping))
				.build()).whenComplete((response, throwable) -> {
					if(null != callback) callback.call(null == response ? null : mapping, throwable);
				});
	}
	
	@Override
	public void saveAll(Collection<Mapping> mappings, Callback<Collection<Mapping>> callback) {
		final List<WriteRequest> requests = mapper.unmap(mappings).stream()
			.map(PutRequest.builder()::item)
			.map(PutRequest.Builder::build)
			.map(WriteRequest.builder()::putRequest)
			.map(WriteRequest.Builder::build)
			.collect(Collectors.toList());
		client.batchWriteItem(BatchWriteItemRequest.builder()
				.requestItems(Collections.singletonMap(tableName, requests))
				.build()).whenComplete((response, throwable) -> {
					if(null != callback) callback.call(mappings, throwable);
				});
	}
	
	@Override
	public void delete(String id, Callback<String> callback) {
		client.deleteItem(DeleteItemRequest.builder()
				.tableName(tableName)
				.key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
				.build()).whenComplete((response, throwable) -> {
					if(null != callback) callback.call(null == response ? null : id, throwable);
				});
	}

}
