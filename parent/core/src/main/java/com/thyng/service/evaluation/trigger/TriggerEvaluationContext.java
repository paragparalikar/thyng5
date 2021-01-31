package com.thyng.service.evaluation.trigger;

import java.util.List;
import java.util.Map;

import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingStatusChange;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TriggerEvaluationContext {

	private final Long now;
	private final List<Thing> things;
	private final Map<String, Long> lastMetricTimestamps;
	private final Map<String, ThingStatusChange> thingStatusChanges;
	
}
