package com.thyng.service;

import java.util.Collection;
import java.util.concurrent.Executor;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Mapping;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.Trigger;
import com.thyng.util.Names;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class TriggerEvaluationService implements Lifecycle {
	
	@NonNull private final Executor executor;
	@NonNull private final CacheService cacheService;
	@NonNull private final EventService eventService;
	@NonNull private final ScriptEvaluationService scriptEvaluationService;
	
	public void evaluateAll() {
		cacheService.findAll(Names.TRIGGER).stream()
			.map(Trigger.class::cast)
			.filter(this::filter)
			.forEach(this::evaluate);
	}
	
	public void evaluate(Trigger trigger) {
		trigger.getThingGroupIds().stream()
			.map(id -> cacheService.findById(id, Names.THING_GROUP_MAPPING))
			.map(Mapping.class::cast)
			.map(Mapping::getValues)
			.flatMap(Collection::stream)
			.map(id -> cacheService.findById(id, Names.THING))
			.map(Thing.class::cast)
			.forEach(thing -> evaluate(trigger, thing));
	}
	
	public void evaluate(Trigger trigger, Thing thing) {
		
	}

	public boolean filter(Trigger trigger) {
		if(!trigger.getEnabled()) return false;
		if(trigger.getActionIds().isEmpty()) return false;
		if(trigger.getThingGroupIds().isEmpty()) return false;
		
		final Tenant tenant = cacheService.findById(trigger.getTenantId(), Names.TENANT);
		if(!tenant.getEnabled()) return false;
		
		final boolean hasEnabledAction = trigger.getActionIds().stream()
			.map(id -> cacheService.findById(id, Names.ACTION))
			.map(Action.class::cast)
			.anyMatch(Action::getEnabled);
		if(!hasEnabledAction) return false;
		
		return true;
	}
	
}
