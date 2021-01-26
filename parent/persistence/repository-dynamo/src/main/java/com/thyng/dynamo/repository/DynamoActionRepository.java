package com.thyng.dynamo.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thyng.domain.model.Action;

import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

@SuperBuilder
public class DynamoActionRepository extends DynamoTenantAwareRepository<Action> {
	
	@Override
	public List<Action> findAll(String tenantId) {
		final Map<String, String> attributeNames = new HashMap<>();
		attributeNames.put("#n", "name");
		attributeNames.put("#t", "type");
		return getClient().query(QueryRequest.builder()
				.tableName(getTableName())
				.select(Select.SPECIFIC_ATTRIBUTES)
				.keyConditionExpression("tenantId = :tenantId")
				.projectionExpression("id,#n,#t")
				.expressionAttributeNames(attributeNames)
				.expressionAttributeValues(Collections.singletonMap(":tenantId", AttributeValue.builder().s(tenantId).build()))
				.build()).join().items().stream()
				.map(getMapper()::map)
				.collect(Collectors.toList());
	}

}
