package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thyng.domain.intf.TemplateAwareModel;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.TemplateAwareRepository;

import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

public class DynamoTemplateAwareRepository<T extends TemplateAwareModel> extends DynamoTenantAwareRepository<T> implements TemplateAwareRepository<T> {

	private final String tableName;
	private final Mapper<T> mapper;
	private final DynamoDbClient client;
	
	public DynamoTemplateAwareRepository(@NonNull String tableName, @NonNull Mapper<T> mapper,
			@NonNull DynamoDbClient client, @NonNull CounterRepository counterRepository) {
		super(tableName, mapper, client, counterRepository);
		this.tableName = tableName;
		this.mapper = mapper;
		this.client = client;
	}

	@Override
	public List<T> findByTemplateId(String tenantId, String templateId) {
		final Map<String, AttributeValue> attributes = new HashMap<>();
		attributes.put(":tenantId", AttributeValue.builder().s(tenantId).build());
		attributes.put(":templateId", AttributeValue.builder().s(templateId).build());
		return client.query(QueryRequest.builder()
				.tableName(tableName)
				.select(Select.ALL_ATTRIBUTES)
				.keyConditionExpression("tenantId = :tenantId")
				.filterExpression("templateId = :templateId")
				.expressionAttributeValues(attributes)
				.build()).items().stream()
				.map(mapper::map)
				.collect(Collectors.toList());
	}
	
}