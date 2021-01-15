package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.thyng.Callback;
import com.thyng.domain.intf.Identifiable;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.Repository;
import com.thyng.util.Strings;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

@RequiredArgsConstructor
public abstract class AbstractDynamoRepository<T extends Identifiable<String>> implements Repository<T, String> {

	private final String tableName;
	private final DynamoDbAsyncClient client;
	private final CounterRepository counterRepository;

	protected abstract Map<String, AttributeValue> map(T item);
	
	protected abstract T map(Map<String, AttributeValue> attributes);

	@Override
	public void count(Callback<Long> callback) {
		final ScanRequest request = ScanRequest.builder()
				.tableName(tableName)
				.select(Select.COUNT)
				.build();
		final AtomicLong counter = new AtomicLong(0);
		client.scanPaginator(request).subscribe(response -> {
			counter.addAndGet(response.count());
		}).whenCompleteAsync((none, throwable) -> {
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
	public void findById(String id, Callback<Optional<T>> callback) {
		final GetItemRequest request = GetItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id))
				.build();
		client.getItem(request).whenCompleteAsync((response, throwable) -> {
			final T item = null == response || null == response.item() ? null : map(response.item());
			final Optional<T> optionalItem = null == throwable ? Optional.ofNullable(item) : null;
			callback.call(optionalItem, throwable); 
		});
	}

	@Override
	public void deleteById(String id, Callback<T> callback) {
		final DeleteItemRequest request = DeleteItemRequest.builder()
				.tableName(tableName)
				.key(keyAttributes(id))
				.build();
		client.deleteItem(request).whenCompleteAsync((response, throwable) -> {
			final T item = null == response || null == response.attributes() ? 
					null : map(response.attributes()); 
			callback.call(item, throwable); 
		});
	}
	
	protected Map<String, AttributeValue> keyAttributes(String id){
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(id).build());
		return map;
	}

	@Override
	public void existsByName(String id, String name, Callback<Boolean> callback) {
		final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":id", AttributeValue.builder().s(id).build());
		expressionAttributeValues.put(":name", AttributeValue.builder().s(name).build());
		final ScanRequest request = ScanRequest.builder()
				.tableName(tableName)
				.select(Select.COUNT)
				.filterExpression("id != :id AND name = :name")
				.expressionAttributeValues(expressionAttributeValues)
				.build();
		final AtomicBoolean flag = new AtomicBoolean(Boolean.FALSE);
		client.scanPaginator(request).subscribe(response -> {
			if(0 < response.count()) {
				flag.set(Boolean.TRUE);
			}
		}).whenComplete((none, throwable) -> {
			callback.call(flag.get(), throwable);
		});
	}
	
}
