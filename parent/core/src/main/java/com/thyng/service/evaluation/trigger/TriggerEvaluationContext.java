package com.thyng.service.evaluation.trigger;

import java.util.List;

import com.thyng.domain.dto.EventDto;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.TriggerInfo;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TriggerEvaluationContext {

	@NonNull private final Trigger trigger;
	@NonNull private final TriggerInfo triggerInfo;
	@NonNull private final List<EventDto> events;
}
