package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.ThingInfo;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ThingInfoMapper implements Mapper<ThingInfo, Map<String, AttributeValue>>{

	@Override
	public ThingInfo map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return ThingInfo.builder()
				.id(map.getS("id"))
				.online(map.getBool("online"))
				.lastStatusChangeTimestamp(map.getLong("lastStatusChangeTimestamp"))
				.build();
	}

	@Override
	public Map<String, AttributeValue> unmap(ThingInfo entity) {
		return null == entity ? null : new AttributeMap(new HashMap<>())
				.put("id", entity.getId())
				.put("online", entity.getOnline())
				.put("lastStatusChangeTimestamp", entity.getLastStatusChangeTimestamp());
	}

}
