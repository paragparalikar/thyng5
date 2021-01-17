package com.thyng.dynamo.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

@Slf4j
@RequiredArgsConstructor
public class DynamoTenantAwareRepository<T extends TenantAwareModel> implements TenantAwareRepository<T> {

	@NonNull private final String tableName;
	@NonNull private final Mapper<T> mapper;
	@NonNull private final DynamoDbClient client;
	@NonNull private final CounterRepository counterRepository;

	@Override
	public List<T> findAll() {
		return client.scan(ScanRequest.builder()
				.tableName(tableName)
				.select(Select.ALL_ATTRIBUTES)
				.build()).items().stream()
				.map(mapper::map)
				.collect(Collectors.toList());
	}

	@Override
	public List<T> findAll(String tenantId) {
		return client.query(QueryRequest.builder()
				.tableName(tableName)
				.select(Select.ALL_ATTRIBUTES)
				.keyConditionExpression("tenantId = :tenantId")
				.expressionAttributeValues(Collections.singletonMap(":tenantId", AttributeValue.builder().s(tenantId).build()))
				.build()).items().stream()
				.map(mapper::map)
				.collect(Collectors.toList());
	}

	private String nextId() {
		return Long.toString(counterRepository.addAndGet(tableName, 1L), Character.MAX_RADIX);
	}
	
	@Override
	public T save(T entity) {
		if(Strings.isBlank(entity.getId())) entity.setId(nextId());
		client.putItem(PutItemRequest.builder()
				.tableName(tableName)
				.item(mapper.unmap(entity))
				.build());
		log.info("Created/Updated entity in table {} as {}", tableName, entity);
		return entity;
	}
	
	protected Map<String, AttributeValue> keyAttributes(String id, String tenantId){
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(id).build());
		map.put("tenantId", AttributeValue.builder().s(tenantId).build());
		return map;
	}

	@Override
	public T findById(String tenantId, String id) {
		return mapper.map(
				client.getItem(GetItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id, tenantId))
				.build()).item());
	}

	@Override
	public T deleteById(String tenantId, String id) {
		return mapper.map(
				client.deleteItem(DeleteItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id, tenantId))
				.build()).attributes());
	}

	@Override
	public boolean existsByName(String tenantId, String id, String name) {
		final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":id", AttributeValue.builder().s(id).build());
		expressionAttributeValues.put(":name", AttributeValue.builder().s(name).build());
		expressionAttributeValues.put(":tenantId", AttributeValue.builder().s(tenantId).build());
		final QueryRequest request = QueryRequest.builder()
				.tableName(tableName)
				.select(Select.COUNT)
				.keyConditionExpression("tenantId = :tenantId AND id != :id")
				.filterExpression("name = :name")
				.expressionAttributeValues(expressionAttributeValues)
				.build();
		return 0 < client.query(request).count();
	}

}
