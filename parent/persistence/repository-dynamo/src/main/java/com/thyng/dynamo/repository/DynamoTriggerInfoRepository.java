package com.thyng.dynamo.repository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.thyng.domain.model.TriggerInfo;

import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.Select;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Slf4j
@SuperBuilder
public class DynamoTriggerInfoRepository extends DynamoRepository<TriggerInfo> {
	
	@Override
	public List<TriggerInfo> findAll() {
		ScanResponse response = null;
		Map<String, AttributeValue> lastEvaluatedKey = null;
		final List<TriggerInfo> infos = new LinkedList<>();
		do {
			response = getClient().scan(ScanRequest.builder()
					.tableName(getTableName())
					.select(Select.ALL_ATTRIBUTES)
					.build()).join();
			response.items().forEach(item -> infos.add(getMapper().map(item)));
			lastEvaluatedKey = response.lastEvaluatedKey();
		} while(null != response
				&& response.hasLastEvaluatedKey()
				&& null != lastEvaluatedKey
				&& !lastEvaluatedKey.isEmpty());
		return infos;
	}
	
	public void updateEvaluationInfo(String id, Long lastEvaluationTime) {
		getClient().updateItem(UpdateItemRequest.builder()
				.tableName(getTableName())
				.key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
				.updateExpression("SET lastEvaluationTime = :lastEvaluationTime, ADD totalEvaluationCount 1")
				.expressionAttributeValues(Collections.singletonMap(":lastEvaluationTime", AttributeValue.builder()
						.n(lastEvaluationTime.toString()).build()))
				.build()).whenComplete((response, throwable) -> {
					if(null != throwable) log.error("Failed to update evaluation info for trigger " + id, throwable);
				});
	}
	
	public void updateMatchInfo(String id, Long lastMatchTime) {
		getClient().updateItem(UpdateItemRequest.builder()
				.tableName(getTableName())
				.key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
				.updateExpression("SET lastMatchTime = :lastMatchTime, ADD totalMatchCount 1")
				.expressionAttributeValues(Collections.singletonMap(":lastMatchTime", AttributeValue.builder()
						.n(lastMatchTime.toString()).build()))
				.build()).whenComplete((response, throwable) -> {
					if(null != throwable) log.error("Failed to update match info for trigger " + id, throwable);
				});
	}

}
