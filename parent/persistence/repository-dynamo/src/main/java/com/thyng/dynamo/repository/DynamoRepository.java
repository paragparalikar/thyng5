package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.thyng.domain.intf.Identifiable;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.Repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.Select;

@Slf4j
@SuperBuilder
@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class DynamoRepository<T extends Identifiable<T>> implements Repository<T> {

	@NonNull private final String tableName;
	@NonNull private final DynamoDbAsyncClient client;
	@NonNull private final Mapper<T, Map<String, AttributeValue>> mapper;

	@Override
	public List<T> findAll() {
		final List<T> items = new LinkedList<>();
		ScanResponse response = null;
		Map<String, AttributeValue> lastEvaluatedKey = null;
		do {
			response = client.scan(ScanRequest.builder()
					.tableName(tableName)
					.select(Select.ALL_ATTRIBUTES)
					.build()).join();
			response.items().stream()
					.map(mapper::map)
					.forEach(items::add);
			lastEvaluatedKey = response.lastEvaluatedKey();
		}while(null != response 
			&& response.hasLastEvaluatedKey()
			&& null != lastEvaluatedKey 
			&& !lastEvaluatedKey.isEmpty());
		return items;
	}

	@Override
	public T save(T entity) {
		client.putItem(PutItemRequest.builder()
				.tableName(tableName)
				.item(mapper.unmap(entity))
				.build());
		log.info("Created/Updated entity in table {} as {}", tableName, entity);
		return entity;
	}

	@Override
	public T findById(String id) {
		return mapper.map(
				client.getItem(GetItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id))
				.build()).join().item());
	}

	@Override
	public T deleteById(String id) {
		return mapper.map(
				client.deleteItem(DeleteItemRequest.builder()
				.tableName(tableName)
				.returnValues(ReturnValue.ALL_OLD)
				.key(keyAttributes(id))
				.build()).join().attributes());
	}
	
	protected Map<String, AttributeValue> keyAttributes(String id){
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(id).build());
		return map;
	}
	
}
