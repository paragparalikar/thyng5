package com.thyng.dynamo.repository;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.enumeration.EventType;
import com.thyng.domain.enumeration.Language;
import com.thyng.domain.enumeration.WindowBase;
import com.thyng.domain.enumeration.WindowType;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.Window;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TriggerDynamoRepository extends AbstractDynamoMultiTenantRepository<Trigger> {

	public TriggerDynamoRepository(DynamoDbAsyncClient client) {
		super(Trigger.CACHE_NAME, client);
	}

	@Override
	protected Map<String, AttributeValue> map(Trigger item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("enabled", AttributeValue.builder().bool(item.getEnabled()).build());
		map.put("language", AttributeValue.builder().s(String.valueOf(item.getLanguage())).build());
		map.put("eventType", AttributeValue.builder().s(item.getEventType().toString()).build());
		map.put("thingSelectionScript", AttributeValue.builder().s(item.getThingSelectionScript()).build());
		map.put("evaluationScript", AttributeValue.builder().s(item.getEvaluationScript()).build());
		if(null != item.getWindow()) map.put("window", AttributeValue.builder().m(map(item.getWindow())).build());
		return map;
	}
	
	protected Map<String, AttributeValue> map(Window window){
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("base", AttributeValue.builder().s(window.getBase().toString()).build());
		map.put("type", AttributeValue.builder().s(window.getType().toString()).build());
		map.put("tumblingInterval", AttributeValue.builder().n(window.getTumblingInterval().toString()).build());
		map.put("slidingInterval", AttributeValue.builder().n(window.getSlidingInterval().toString()).build());
		return map;
	}

	@Override
	protected Trigger map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Trigger item = new Trigger();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		item.setTenantId(attributes.get("tenantId").s());
		item.setEnabled(attributes.get("enabled").bool());
		item.setWindow(mapWindow(attributes.get("window").m()));
		item.setLanguage(Language.valueOf(attributes.get("language").s()));
		item.setEventType(EventType.valueOf(attributes.get("eventType").s()));
		item.setThingSelectionScript(attributes.get("thingSelectionScript").s());
		item.setEvaluationScript(attributes.get("evaluationScript").s());
		return item;
	}
	
	protected Window mapWindow(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Window item = new Window();
		item.setBase(WindowBase.valueOf(attributes.get("base").s()));
		item.setType(WindowType.valueOf(attributes.get("type").s()));
		item.setSlidingInterval(Long.parseLong(attributes.get("slidingInterval").n()));
		item.setTumblingInterval(Long.parseLong(attributes.get("tumblingInterval").n()));
		return item;
	}

}
