package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.thyng.domain.model.ThingStatusChange;
import com.thyng.domain.model.ThingStatusChangeInfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class ThingStatusChangeHistoryMapper implements Mapper<ThingStatusChangeInfo, Map<String, AttributeValue>> {
	
	@NonNull private final Mapper<ThingStatusChange, String> thingStatusChangeMapper;

	@Override
	public ThingStatusChangeInfo map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		final Map<String, ThingStatusChange> changes = map.getSs("values").stream()
				.map(thingStatusChangeMapper::map)
				.collect(Collectors.toMap(ThingStatusChange::getId, Function.identity()));
		return ThingStatusChangeInfo.builder()
					.id(map.getS("id"))
					.changes(changes)
					.build();
	}

	@Override
	public Map<String, AttributeValue> unmap(ThingStatusChangeInfo entity) {
		return null == entity ? null : new AttributeMap(new HashMap<>())
				.put("id", entity.getId())
				.put("values", entity.getChanges().values().stream()
						.map(thingStatusChangeMapper::unmap)
						.collect(Collectors.toSet()));
	}

}
