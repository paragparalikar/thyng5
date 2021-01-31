package com.thyng.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.thyng.domain.intf.Identifiable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class TriggerEvaluationInfo implements Serializable, Identifiable<TriggerEvaluationInfo> {
	private static final long serialVersionUID = 4342430427227497219L;

	@NonNull private final String id;
	@Builder.Default private final Map<String, Long> lastEvalutionTimes = Collections.emptyMap();
	@Builder.Default private final Map<String, Long> lastEvalutionEventTimes = Collections.emptyMap();
	
}
