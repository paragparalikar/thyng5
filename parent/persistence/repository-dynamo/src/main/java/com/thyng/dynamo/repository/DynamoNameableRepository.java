package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;
import com.thyng.repository.NameableRepository;

import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.Select;

@SuperBuilder(builderMethodName = "nameableRepositoryBuilder")
public class DynamoNameableRepository<T extends Identifiable<T> & Nameable> extends DynamoIdGenerationRepository<T> implements NameableRepository<T>{
	
	@Override
	public boolean existsByName(String id, String name) {
		final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":id", AttributeValue.builder().s(id).build());
		expressionAttributeValues.put(":name", AttributeValue.builder().s(name).build());
		final ScanRequest request = ScanRequest.builder()
				.tableName(getTableName())
				.select(Select.COUNT)
				.filterExpression("id != :id AND name = :name")
				.expressionAttributeValues(expressionAttributeValues)
				.build();
		return 0 < getClient().scan(request).join().count();
	}
	
}
