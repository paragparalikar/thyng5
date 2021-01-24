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
public class TriggerMapper implements Mapper<Trigger, Map<String, AttributeValue>> {

	@NonNull private final WindowMapper windowMapper;
	
	@Override
	public Trigger map(Map<String, AttributeValue> attributes) {
		final AttributeMap map = new AttributeMap(attributes);
		final Trigger trigger = Trigger.builder()
			.id(map.getS("id"))
			.name(map.getS("name"))
			.script(map.getS("script"))
			.tenantId(map.getS("tenantId"))
			.enabled(map.getBool("enabled"))
			.window(windowMapper.map(map.getM("window")))
			.includeEvents(map.getBool("includeEvents"))
			.includeAggregations(map.getBool("includeAggregations"))
			.language(map.getEnum("language", Language.class))
			.eventType(map.getEnum("eventType", EventType.class))
			.build();
		trigger.getActionIds().addAll(map.getSs("actionIds"));
		trigger.getThingGroupIds().addAll(map.getSs("thingGroupIds"));
		return trigger;
	}
	
	@Override
	public Map<String, AttributeValue> unmap(Trigger item) {
		return null == item ? null : new AttributeMap(new HashMap<>())
				.put("id", item.getId())
				.put("name", item.getName())
				.put("script", item.getScript())
				.put("tenantId", item.getTenantId())
				.put("enabled", item.getEnabled())
				.put("window", windowMapper.unmap(item.getWindow()))
				.put("language", item.getLanguage())
				.put("eventType", item.getEventType())
				.put("actionIds", item.getActionIds())
				.put("thingGroupIds", item.getThingGroupIds())
				.put("includeEvents", item.getIncludeEvents())
				.put("includeAggregations", item.getIncludeAggregations());
	}
}
