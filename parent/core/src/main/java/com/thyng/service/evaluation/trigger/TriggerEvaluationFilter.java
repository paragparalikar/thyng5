package com.thyng.service.evaluation.trigger;

import java.util.function.BiPredicate;

import com.thyng.domain.enumeration.WindowBase;
import com.thyng.domain.enumeration.WindowType;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.TriggerEvaluationInfo;
import com.thyng.domain.model.TriggerInfo;
import com.thyng.domain.model.Window;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class TriggerEvaluationFilter implements BiPredicate<Trigger, TriggerInfo> {

	@NonNull private Long now;
	@NonNull private final TriggerEvaluationInfo triggerEvaluationInfo; 
	
	@Override
	public boolean test(Trigger trigger, TriggerInfo triggerInfo) {
		if(!triggerInfo.getEligible()) return false;	// Trigger is not eligible
		if(null == trigger.getWindow()) return true;	// Trigger is to be evaluated per event
		final Window window = trigger.getWindow();		// If we are here, it means the trigger is windowed
		if(WindowBase.COUNT.equals(window.getBase())) return true;
		if(WindowType.SESSION.equals(window.getType())) return true;
		final long lastEvaluationTimestamp = triggerEvaluationInfo.getLastEvalutionTimes().getOrDefault(trigger.getId(), 0l);
		return now >= (lastEvaluationTimestamp + window.toMillis());
	}
	
}
