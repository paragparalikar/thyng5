package com.thyng.service.evaluation.trigger;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Source;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Mapping;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.TriggerEvaluationInfo;
import com.thyng.domain.model.TriggerInfo;
import com.thyng.event.EventBus;
import com.thyng.repository.ObjectRepository;
import com.thyng.service.evaluation.ScriptEvaluationService;
import com.thyng.util.Constant;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class TriggerEvaluationService implements Lifecycle {
	
	@NonNull private final EventBus eventService;
	@NonNull private final Source<Action> actionSource;
	@NonNull private final Source<Tenant> tenantSource;
	@NonNull private final Source<Trigger> triggerSource;
	@NonNull private final Source<ThingGroup> thingGroupSource;
	@NonNull private final Source<TriggerInfo> triggerInfoSource;
	@NonNull private final Source<Mapping> thingGroupMappingSource;
	@NonNull private final ScriptEvaluationService scriptEvaluationService;
	@NonNull private final ObjectRepository<TriggerEvaluationInfo> triggerEvaluationInfoRepository;
	
	public void evaluateAll(TriggerEvaluationContext context) throws Exception {
		final ExecutorService executor = Executors.newWorkStealingPool(20);
		final TriggerEvaluationInfo triggerEvaluationInfo = getTriggerEvaluationInfo();
		final TriggerEvaluationFilter triggerEvaluationFilter = TriggerEvaluationFilter.builder()
				.triggerEvaluationInfo(triggerEvaluationInfo)
				.now(context.getNow())
				.build();
	
		for(Trigger trigger : triggerSource.findAll()) {
			final TriggerInfo triggerInfo = triggerInfoSource.findById(trigger.getId());
			if(triggerEvaluationFilter.test(trigger, triggerInfo)) {
				
			}
		}
		
		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.MINUTES);
	}
	
	private TriggerEvaluationInfo getTriggerEvaluationInfo() {
		return Optional.ofNullable(triggerEvaluationInfoRepository.get(Constant.TRIGGER_EVALUATION_INFO))
				.orElse(TriggerEvaluationInfo.builder().id(Constant.TRIGGER_EVALUATION_INFO).build());
	}
	
}