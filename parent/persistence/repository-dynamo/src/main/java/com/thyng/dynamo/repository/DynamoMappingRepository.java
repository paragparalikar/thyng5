package com.thyng.dynamo.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.thyng.domain.model.Mapping;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.MappingRepository;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.Select;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

@Builder
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
	public Mapping findById(String id) {
		final GetItemResponse response = client.getItem(GetItemRequest.builder()
				.tableName(tableName)
				.key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
				.build()).join();
		return mapper.map(response.item());
	}
	
	@Override
	public CompletableFuture<Mapping> save(Mapping mapping) {
		return client.putItem(PutItemRequest.builder()
				.tableName(tableName)
				.item(mapper.unmap(mapping))
				.build())
				.thenApply(response -> mapping);
	}
	
	@Override
	public CompletableFuture<Collection<Mapping>> saveAll(Collection<Mapping> mappings) {
		final List<WriteRequest> requests = mapper.unmap(mappings).stream()
			.map(PutRequest.builder()::item)
			.map(PutRequest.Builder::build)
			.map(WriteRequest.builder()::putRequest)
			.map(WriteRequest.Builder::build)
			.collect(Collectors.toList());
		return client.batchWriteItem(BatchWriteItemRequest.builder()
				.requestItems(Collections.singletonMap(tableName, requests))
				.build())
				.thenApply(response -> mappings);
	}
	
	@Override
	public CompletableFuture<String> delete(String id) {
		return client.deleteItem(DeleteItemRequest.builder()
				.tableName(tableName)
				.key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
				.build())
				.thenApply(response -> id);
	}

}
