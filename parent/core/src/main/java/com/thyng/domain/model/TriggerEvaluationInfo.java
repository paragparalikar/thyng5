package com.thyng.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.thyng.domain.intf.Identifiable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
public class TriggerEvaluationInfo implements Serializable, Identifiable<TriggerEvaluationInfo> {
	private static final long serialVersionUID = 4342430427227497219L;

	@NonNull private final String id;
	@Builder.Default private final Map<String, Long> lastEvalutionTimes = Collections.emptyMap();
	@Builder.Default private final Map<String, Long> lastEvalutionEventTimes = Collections.emptyMap();
	// TODO trigger id is being duplicated here. This will consume resources like storage space and RCUs and WCUs.
	// A domain model should be created for these values and a corresponding mapper.
	
}
