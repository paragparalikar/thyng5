package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.enumeration.EventType;
import com.thyng.domain.enumeration.Language;
import com.thyng.domain.model.Trigger;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class TriggerMapper implements Mapper<Trigger> {

	@NonNull private final WindowMapper windowMapper;
	
	@Override
	public Trigger map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Trigger item = new Trigger();
		item.setId(attributes.get("id").s());
		item.setName(attributes.get("name").s());
		item.setTenantId(attributes.get("tenantId").s());
		item.setEnabled(attributes.get("enabled").bool());
		item.setWindow(windowMapper.map(attributes.get("window").m()));
		item.setLanguage(Language.valueOf(attributes.get("language").s()));
		item.setEventType(EventType.valueOf(attributes.get("eventType").s()));
		item.setThingSelectionScript(attributes.get("thingSelectionScript").s());
		item.setEvaluationScript(attributes.get("evaluationScript").s());
		return item;
	}
	
	@Override
	public Map<String, AttributeValue> unmap(Trigger item) {
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("enabled", AttributeValue.builder().bool(item.getEnabled()).build());
		map.put("language", AttributeValue.builder().s(String.valueOf(item.getLanguage())).build());
		map.put("eventType", AttributeValue.builder().s(item.getEventType().toString()).build());
		map.put("thingSelectionScript", AttributeValue.builder().s(item.getThingSelectionScript()).build());
		map.put("evaluationScript", AttributeValue.builder().s(item.getEvaluationScript()).build());
		map.put("window", AttributeValue.builder().m(windowMapper.unmap(item.getWindow())).build());
		return map;
	}
}
