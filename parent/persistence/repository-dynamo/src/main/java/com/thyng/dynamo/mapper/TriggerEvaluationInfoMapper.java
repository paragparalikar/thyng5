package com.thyng.dynamo.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thyng.domain.model.TriggerEvaluationInfo;
import com.thyng.util.Dates;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TriggerEvaluationInfoMapper implements Mapper<TriggerEvaluationInfo, Map<String, AttributeValue>> {
	
	@Override
	public TriggerEvaluationInfo map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return TriggerEvaluationInfo.builder()
				.id(map.getS("id"))
				.lastEvalutionTimes(parse(map.getSs("lastEvalutionTimes")))
				.lastEvalutionEventTimes(parse(map.getSs("lastEvalutionEventTimes")))
				.build();
	}
	
	private Map<String, Long> parse(List<String> values){
		if(null == values || values.isEmpty()) return null;
		final Map<String, Long> valueMap = new HashMap<>();
		for(final String value : values) {
			final String[] tokens = value.split(":");
			final long timestamp = Dates.decodeBySeconds(tokens[1]);
			valueMap.put(tokens[0], timestamp);
		}
		return valueMap;
	}
	
	private Collection<String> format(Map<String, Long> values){
		final List<String> results = new ArrayList<>(values.size());
		for(Entry<String, Long> entry : values.entrySet()) {
			results.add(entry.getKey() + ":" + Dates.encodeToSeconds(entry.getValue()));
		}
		return results;
	}

	@Override
	public Map<String, AttributeValue> unmap(TriggerEvaluationInfo entity) {
		return null == entity ? null : new AttributeMap(new HashMap<>())
				.put("id", entity.getId())
				.put("lastEvalutionTimes", format(entity.getLastEvalutionTimes()))
				.put("lastEvalutionEventTimes", format(entity.getLastEvalutionEventTimes()));
	}

}
