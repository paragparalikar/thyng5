package com.thyng.domain.model;

import com.thyng.domain.intf.Identifiable;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class TriggerInfo implements Identifiable<TriggerInfo> {

	private final String id;
	private final Long lastMatchTime;
	private final Long totalMatchCount;
	private final Long lastEvaluationTime;
	private final Long totalEvaluationCount;
	
}
