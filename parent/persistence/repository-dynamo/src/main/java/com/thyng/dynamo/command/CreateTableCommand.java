package com.thyng.dynamo.command;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiConsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@Slf4j
@RequiredArgsConstructor
public class CreateTableCommand {

	private final String tableName;
	
	protected ProvisionedThroughput provisionedThroughput() {
		return ProvisionedThroughput.builder()
				.readCapacityUnits(5L)
				.writeCapacityUnits(5L)
				.build();
	}

	protected AttributeDefinition idAttribute() {
		return AttributeDefinition.builder()
				.attributeName("id")
				.attributeType(ScalarAttributeType.S)
				.build();
	}
	
	protected Collection<AttributeDefinition> attributeDefinitions() {
		return Collections.singleton(idAttribute());
	}
	
	protected KeySchemaElement idKey() {
		return KeySchemaElement.builder()
				.attributeName("id")
				.keyType(KeyType.HASH)
				.build();
	}
	
	protected Collection<KeySchemaElement> keySchema(){
		return Collections.singleton(idKey());
	}
	
	protected CreateTableRequest createTableRequest() {
		return CreateTableRequest.builder()
				.tableName(tableName)
				.provisionedThroughput(provisionedThroughput())
				.attributeDefinitions(attributeDefinitions())
				.keySchema(keySchema())
				.build();
	}
	
	public CreateTableResponse execute(DynamoDbClient client) {
		log.info("Creting table {}", tableName);
		return client.createTable(createTableRequest());
	}
	
	public void execute(DynamoDbAsyncClient client, BiConsumer<CreateTableResponse, Throwable> callback) {
		log.info("Creting table {}", tableName);
		client.createTable(createTableRequest()).whenComplete((response, exception) -> {
			callback.accept(response, exception);
		});
	}
	
}
