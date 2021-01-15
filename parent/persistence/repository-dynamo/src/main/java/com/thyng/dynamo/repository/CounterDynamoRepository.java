package com.thyng.dynamo.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.thyng.Callback;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Actuator;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Sensor;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.Trigger;
import com.thyng.repository.CounterRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.Select;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

@RequiredArgsConstructor
public class CounterDynamoRepository implements CounterRepository {
	
	@NonNull private final DynamoDbAsyncClient client;
	
	@Override
	public void start() throws Exception {
		client.scan(ScanRequest.builder()
				.select(Select.ALL_ATTRIBUTES)
				.tableName(CounterRepository.CACHE_NAME)
				.build())
			.thenApply(this::existingCounters)
			.thenApply(this::writeRequests)
			.thenApply(requests -> Collections.singletonMap(CounterRepository.CACHE_NAME, requests))
			.thenApply(BatchWriteItemRequest.builder()::requestItems)
			.<BatchWriteItemRequest>thenApply(BatchWriteItemRequest.Builder::build)
			.thenAccept(client::batchWriteItem)
			.join();
	}
	
	private List<WriteRequest> writeRequests(Set<String> existingCounters){
		return globalCounters().stream()
		.filter(name -> !existingCounters.contains(name))
		.map(this::putRequest)
		.map(WriteRequest.builder()::putRequest)
		.map(WriteRequest.Builder::build)
		.collect(Collectors.toList());
	}
	
	private Set<String> globalCounters(){
		return new HashSet<>(Arrays.asList(
				Tenant.CACHE_NAME, Gateway.CACHE_NAME, Template.CACHE_NAME, 
				Sensor.CACHE_NAME, Actuator.CACHE_NAME, Thing.CACHE_NAME, 
				Trigger.CACHE_NAME, Action.CACHE_NAME));
	}
	
	private Set<String> existingCounters(ScanResponse response){
		return response.items().stream()
				.map(Map::keySet)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}
	
	private PutRequest putRequest(String name) {
		final Map<String, AttributeValue> attributes = new HashMap<>();
		attributes.put("id", AttributeValue.builder().s(name).build());
		attributes.put("val", AttributeValue.builder().n("0").build());
		return PutRequest.builder().item(attributes).build(); 
	}
	
	@Override
	public void get(@NonNull String name, @NonNull Callback<Long> callback) {
		client.getItem(GetItemRequest.builder()
				.consistentRead(true)
				.tableName(CounterRepository.CACHE_NAME)
				.key(Collections.singletonMap("id", AttributeValue.builder().s(name).build()))
				.build()).whenCompleteAsync((response, throwable) -> {
					callback.call(null == response ? null : map(response.item()), throwable);
				});
	}
	
	private Long map(Map<String, AttributeValue> attributes) {
		if(null == attributes) return null;
		if(attributes.isEmpty()) return null;
		final AttributeValue value = attributes.get("val");
		if(null == value) return null;
		return Long.parseLong(value.n());
	}
	

	@Override
	public void addAndGet(@NonNull final String name, @NonNull final Long delta, final Callback<Long> callback) {
		client.updateItem(UpdateItemRequest.builder()
				.tableName(CounterRepository.CACHE_NAME)
				.key(Collections.singletonMap("id", AttributeValue.builder().s(name).build()))
				.returnValues(ReturnValue.ALL_NEW)
				.updateExpression("SET val = val + :val")
				.expressionAttributeValues(Collections.singletonMap(":val", AttributeValue.builder().n(delta.toString()).build()))
				.build()).whenCompleteAsync((response, throwable) -> {
					callback.call(null == response ? null : map(response.attributes()), throwable);
				});
	}

}
