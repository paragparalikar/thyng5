package com.thyng.dynamo.mapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thyng.domain.model.TriggerEvaluationInfo;
import com.thyng.util.Base62;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TriggerEvaluationInfoMapper implements Mapper<TriggerEvaluationInfo, Map<String, AttributeValue>> {
	
	private final long epoch;
	
	public TriggerEvaluationInfoMapper() {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(2021, 0, 1, 0, 0, 0);
		this.epoch = Duration.ofMillis(calendar.getTimeInMillis()).getSeconds();
	}

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
			final long timestamp = (epoch + Base62.decode(tokens[1]))*1000;
			valueMap.put(tokens[0], timestamp);
		}
		return valueMap;
	}
	
	private Collection<String> format(Map<String, Long> values){
		final List<String> results = new ArrayList<>(values.size());
		for(Entry<String, Long> entry : values.entrySet()) {
			final long seconds = entry.getValue()/1000;
			results.add(entry.getKey() + ":" + Base62.encode(seconds - epoch));
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
