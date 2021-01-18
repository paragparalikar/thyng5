package com.thyng.dynamo.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.Repository;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

@Slf4j
@RequiredArgsConstructor
public class DynamoRepository<T extends Identifiable<String> & Nameable> implements Repository<T, String> {

	@NonNull private final String tableName;
	@NonNull private final Mapper<T, Map<String, AttributeValue>> mapper;
	@NonNull private final DynamoDbClient client;
	@NonNull private final CounterRepository counterRepository;

	@Override
	public List<T> findAll() {
		return client.scan(ScanRequest.builder()
				.tableName(tableName)
				.select(Select.SPECIFIC_ATTRIBUTES)
				.projectionExpression("id,#n")
				.expressionAttributeNames(Collections.singletonMap("#n", "name"))
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

	@Override
	public T findById(String id) {
		return mapper.map(
				client.getItem(GetItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id))
				.build()).item());
	}

	@Override
	public T deleteById(String id) {
		return mapper.map(
				client.deleteItem(DeleteItemRequest.builder()
				.tableName(tableName)
				.returnValues(ReturnValue.ALL_OLD)
				.key(keyAttributes(id))
				.build()).attributes());
	}

	@Override
	public boolean existsByName(String id, String name) {
		final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":id", AttributeValue.builder().s(id).build());
		expressionAttributeValues.put(":name", AttributeValue.builder().s(name).build());
		final ScanRequest request = ScanRequest.builder()
				.tableName(tableName)
				.select(Select.COUNT)
				.filterExpression("id != :id AND name = :name")
				.expressionAttributeValues(expressionAttributeValues)
				.build();
		return 0 < client.scan(request).count();
	}
	
	protected Map<String, AttributeValue> keyAttributes(String id){
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(id).build());
		return map;
	}

}
