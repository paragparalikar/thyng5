package com.thyng.service;

import java.util.concurrent.Executor;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Source;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Mapping;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.Trigger;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class TriggerEvaluationService implements Lifecycle {
	
	@NonNull private final Executor executor;
	@NonNull private final EventService eventService;
	@NonNull private final Source<Action> actionSource;
	@NonNull private final Source<Tenant> tenantSource;
	@NonNull private final Source<Trigger> triggerSource;
	@NonNull private final Source<ThingGroup> thingGroupSource;
	@NonNull private final Source<Mapping> thingGroupMappingSource;
	@NonNull private final ScriptEvaluationService scriptEvaluationService;
	
	public void evaluateAll() {
		triggerSource.findAll().stream()
			.map(Trigger.class::cast)
			.filter(this::filter)
			.forEach(this::evaluate);
	}
	
	public void evaluate(Trigger trigger) {
		
	}
	
	public void evaluate(Trigger trigger, Thing thing) {
		
	}

	public boolean filter(Trigger trigger) {
		if(!trigger.getEnabled()) return false;
		if(trigger.getActionIds().isEmpty()) return false;
		if(trigger.getThingGroupIds().isEmpty()) return false;
		
		final Tenant tenant = tenantSource.findById(trigger.getTenantId());
		if(!tenant.getEnabled()) return false;
		
		final boolean hasEnabledAction = trigger.getActionIds().stream()
			.map(id -> actionSource.findById(id))
			.map(Action.class::cast)
			.anyMatch(Action::getEnabled);
		if(!hasEnabledAction) return false;
		
		return true;
	}
	
}
