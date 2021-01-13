package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.thyng.domain.intf.Callback;
import com.thyng.repository.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDynamoRepository<T> implements Repository<T, String> {

	private final String tableName;
	private final DynamoDbAsyncClient client;

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
			callback.after(counter.get(), throwable);
		});
	}

	@Override
	public void findAll(Callback<List<T>> callback) {
		final ScanRequest request = ScanRequest.builder()
				.tableName(tableName)
				.select(Select.ALL_ATTRIBUTES)
				.build();
		client.scanPaginator(request).subscribe(response -> {
			callback.partial(response.items().stream().map(this::map).collect(Collectors.toList()));
		}).whenCompleteAsync((none, throwable) -> {
			callback.after(null, throwable);
		});
	}

	@Override
	public void save(T entity, Callback<T> callback) {
		client.putItem(PutItemRequest.builder()
				.tableName(tableName)
				.item(map(entity))
				.build()).whenCompleteAsync((response, throwable) -> {
					final T item = null == response ? null : map(response.attributes()); 
					callback.after(item, throwable);
					log.info("Saved {} with success {} and failure {}", tableName, item, throwable);
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
			callback.after(optionalItem, throwable); 
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
			callback.after(item, throwable); 
			log.info("Deleted	 {} with success {} and failure {}", tableName, item, throwable);
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
			callback.after(flag.get(), throwable);
		});
	}
	
}
