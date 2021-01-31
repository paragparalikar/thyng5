package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.TriggerInfo;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TriggerInfoMapper implements Mapper<TriggerInfo, Map<String, AttributeValue>> {

	@Override
	public TriggerInfo map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return TriggerInfo.builder()
				.id(map.getS("id"))
				.eligible(map.getBool("eligible"))
				.lastMatchTime(map.getLong("lastMatchTime"))
				.totalMatchCount(map.getLong("totalMatchCount"))
				.build();
	}

	@Override
	public Map<String, AttributeValue> unmap(TriggerInfo entity) {
		return null == entity ? null : new AttributeMap(new HashMap<>())
				.put("id", entity.getId())
				.put("eligible", entity.getEligible())
				.put("lastMatchTime", entity.getLastMatchTime())
				.put("totalMatchCount", entity.getTotalMatchCount());
	}

}
