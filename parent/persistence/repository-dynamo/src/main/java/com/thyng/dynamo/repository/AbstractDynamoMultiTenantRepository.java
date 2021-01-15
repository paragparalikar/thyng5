package com.thyng.dynamo.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.thyng.Callback;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.MultiTenantRepository;
import com.thyng.util.Strings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDynamoMultiTenantRepository<T extends TenantAwareModel> implements MultiTenantRepository<T> {

	private final String tableName;
	private final DynamoDbAsyncClient client;
	private final CounterRepository counterRepository;

	protected abstract Map<String, AttributeValue> map(T item);
	
	protected abstract T map(Map<String, AttributeValue> attributes);

	@Override
	public void count(String tenantId, Callback<Long> callback) {
		final QueryRequest queryRequest = QueryRequest.builder()
				.tableName(tableName)
				.select(Select.COUNT)
				.keyConditionExpression("tenantId = :tenantId")
				.expressionAttributeValues(Collections.singletonMap(":tenantId", AttributeValue.builder().s(tenantId).build()))
				.build();
		final AtomicLong counter = new AtomicLong(0);
		client.queryPaginator(queryRequest).subscribe(response -> {
			counter.addAndGet(response.count());
		}).whenCompleteAsync((none, throwable) -> {
			if(null != throwable) log.error("Failed to count {} for tenantId {} with exception {}", 
					tableName, tenantId, throwable.getMessage());
			callback.call(counter.get(), throwable);
		});
	}

	@Override
	public void findAll(Callback<List<T>> callback) {
		final ScanRequest request = ScanRequest.builder()
				.tableName(tableName)
				.select(Select.ALL_ATTRIBUTES)
				.build();
		final List<T> items = new LinkedList<>();
		client.scanPaginator(request).subscribe(response -> {
			response.items().stream().map(this::map).forEach(items::add);
		}).whenCompleteAsync((none, throwable) -> {
			callback.call(null == throwable ? items : null, throwable);
		});
	}
	
	@Override
	public void findAll(String tenantId, Callback<List<T>> callback) {
		final QueryRequest queryRequest = QueryRequest.builder()
				.tableName(tableName)
				.select(Select.ALL_ATTRIBUTES)
				.keyConditionExpression("tenantId = :tenantId")
				.expressionAttributeValues(Collections.singletonMap(":tenantId", AttributeValue.builder().s(tenantId).build()))
				.build();
		final List<T> items = new LinkedList<>();
		client.queryPaginator(queryRequest).subscribe(response -> {
			response.items().stream().map(this::map).forEach(items::add);
		}).whenCompleteAsync((none, throwable) -> {
			callback.call(null == throwable ? items : null, throwable);
		});
	}

	@Override
	public void save(T entity, Callback<T> callback) {
		if(Strings.isBlank(entity.getId())) {
			counterRepository.addAndGet(tableName, 1L, Callback.<Long>builder()
					.failure(callback.getFailure())
					.after(callback.getAfter())
					.success(id -> {
						entity.setId(Long.toString(id, Character.MAX_RADIX));
						save_(entity, callback);
					})
					.build());
		} else {
			save_(entity, callback);
		}
	}
	
	private void save_(T entity, Callback<T> callback) {
		client.putItem(PutItemRequest.builder()
				.tableName(tableName)
				.item(map(entity))
				.build()).whenCompleteAsync((response, throwable) -> {
					callback.call(null == throwable ? entity : null, throwable);
				});
		}

	@Override
	public void findById(String tenantId, String id, Callback<Optional<T>> callback) {
		final GetItemRequest request = GetItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id, tenantId))
				.build();
		client.getItem(request).whenCompleteAsync((response, throwable) -> {
			final T item = null == response ? null : map(response.item());
			final Optional<T> optionalItem = null == throwable ? Optional.ofNullable(item) : null;
			callback.call(optionalItem, throwable); 
		});
	}

	@Override
	public void deleteById(String tenantId, String id, Callback<T> callback) {
		final DeleteItemRequest request = DeleteItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id, tenantId))
				.build();
		client.deleteItem(request).whenCompleteAsync((response, throwable) -> {
			final T item = null == response ? null : map(response.attributes());
			callback.call(item, throwable);  
			log.info("Deleted {} with success {} and failure {}", tableName, item, throwable);
		});
	}
	
	protected Map<String, AttributeValue> keyAttributes(String id, String tenantId){
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(id).build());
		map.put("tenantId", AttributeValue.builder().s(tenantId).build());
		return map;
	}
	
	@Override
	public void existsByName(String tenantId, String id, String name, Callback<Boolean> callback) {
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
		final AtomicBoolean flag = new AtomicBoolean(Boolean.FALSE);
		client.queryPaginator(request).subscribe(response -> {
			if(0 < response.count()) {
				flag.set(Boolean.TRUE);
			}
		}).whenComplete((none, throwable) -> {
			callback.call(flag.get(), throwable);
		});
	}

}
