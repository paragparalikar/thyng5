package com.thyng.dynamo.command;

import java.util.Arrays;
import java.util.Collection;

import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class CreateTenantAwareTableCommand extends CreateTableCommand {

	public CreateTenantAwareTableCommand(String tableName) {
		super(tableName);
	}
	
	@Override
	protected KeySchemaElement idKey() {
		return KeySchemaElement.builder()
				.attributeName("id")
				.keyType(KeyType.RANGE)
				.build();
	}
	
	protected AttributeDefinition tenantIdAttribute() {
		return AttributeDefinition.builder()
				.attributeName("tenantId")
				.attributeType(ScalarAttributeType.S)
				.build();
	}
	
	protected KeySchemaElement tenantIdKey() {
		return KeySchemaElement.builder()
				.attributeName("tenantId")
				.keyType(KeyType.HASH)
				.build();
	}
	
	@Override
	protected Collection<AttributeDefinition> attributeDefinitions() {
		return Arrays.asList(tenantIdAttribute(), idAttribute());
	}
	
	@Override
	protected Collection<KeySchemaElement> keySchema() {
		return Arrays.asList(tenantIdKey(), idKey());
	}

}
