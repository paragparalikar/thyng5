package com.thyng.service.evaluation.trigger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Source;
import com.thyng.domain.intf.TenantAwareSource;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.TriggerInfo;
import com.thyng.event.EventBus;
import com.thyng.repository.Repository;
import com.thyng.util.Constant;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TriggerEvaluationEligibilityService implements Lifecycle {

	@NonNull private final EventBus eventService;
	@NonNull private final Source<Tenant> tenantSource;
	@NonNull private final TenantAwareSource<Action> actionSource;
	@NonNull private final TenantAwareSource<Trigger> triggerSource;
	@NonNull private final Repository<TriggerInfo> triggerInfoRepository;
	
	private final Consumer<Action> processByActionCallback = this::processByAction;
	private final Consumer<Tenant> processByTenantCallback = this::processByTenant;
	private final Consumer<Tenant> disableByTenantCallback = this::disableByTenant;
	private final Consumer<Trigger> processTriggerCallback = this::processTrigger;
	private final Consumer<Trigger> disableTriggerCallback = this::disableTrigger;
	
	@Override
	public void start() throws Exception {
		eventService.subscribe(Constant.createdTopic(Constant.ACTION), processByActionCallback);
		eventService.subscribe(Constant.updatedTopic(Constant.ACTION), processByActionCallback);
		eventService.subscribe(Constant.deletedTopic(Constant.ACTION), processByActionCallback);
		eventService.subscribe(Constant.createdTopic(Constant.TENANT), processByTenantCallback);
		eventService.subscribe(Constant.updatedTopic(Constant.TENANT), processByTenantCallback);
		eventService.subscribe(Constant.deletedTopic(Constant.TENANT), disableByTenantCallback);
		eventService.subscribe(Constant.createdTopic(Constant.TRIGGER), processTriggerCallback);
		eventService.subscribe(Constant.updatedTopic(Constant.TRIGGER), processTriggerCallback);
		eventService.subscribe(Constant.deletedTopic(Constant.TRIGGER), disableTriggerCallback);
	}
	
	@Override
	public void stop() throws Exception {
		eventService.unsubscribe(Constant.createdTopic(Constant.ACTION), processByActionCallback);
		eventService.unsubscribe(Constant.updatedTopic(Constant.ACTION), processByActionCallback);
		eventService.unsubscribe(Constant.deletedTopic(Constant.ACTION), processByActionCallback);
		eventService.unsubscribe(Constant.createdTopic(Constant.TENANT), processByTenantCallback);
		eventService.unsubscribe(Constant.updatedTopic(Constant.TENANT), processByTenantCallback);
		eventService.unsubscribe(Constant.deletedTopic(Constant.TENANT), disableByTenantCallback);
		eventService.unsubscribe(Constant.createdTopic(Constant.TRIGGER), processTriggerCallback);
		eventService.unsubscribe(Constant.updatedTopic(Constant.TRIGGER), processTriggerCallback);
		eventService.unsubscribe(Constant.deletedTopic(Constant.TRIGGER), disableTriggerCallback);
	}
	
	private boolean process(Trigger trigger) {
		if(!trigger.getEnabled()) return false;
		if(trigger.getActionIds().isEmpty()) return false;
		if(trigger.getThingGroupIds().isEmpty()) return false;
		
		final Tenant tenant = tenantSource.findById(trigger.getTenantId());
		if(!tenant.getEnabled()) return false;
		
		return trigger.getActionIds().stream()
			.map(id -> actionSource.findById(id))
			.map(Action.class::cast)
			.anyMatch(Action::getEnabled);
	}
	
	private void processAll(Collection<Trigger> triggers) {
		final Collection<TriggerInfo> infos = new HashSet<>();
		for(Trigger trigger : triggers) {
			final boolean eligibility = process(trigger);
			final TriggerInfo info = triggerInfoRepository.findById(trigger.getId());
			if(!Objects.equals(eligibility, info.getEligible())) {
				infos.add(info.withEligible(eligibility));
			}
		}
		triggerInfoRepository.saveAll(infos);
	}
	
	private void disableAll(Collection<Trigger> triggers) {
		final Collection<TriggerInfo> entities = triggers.stream()
			.map(Trigger::getId)
			.map(triggerInfoRepository::findById)
			.filter(Objects::nonNull)
			.map(info -> {
				return info.withEligible(Boolean.FALSE);
			}).collect(Collectors.toSet());
		triggerInfoRepository.saveAll(entities);
	}
	
	private void processTrigger(Trigger trigger) {
		processAll(Collections.singleton(trigger));
	}
	
	private void disableTrigger(Trigger trigger) {
		disableAll(Collections.singleton(trigger));
	}
	
	private void processByTenant(Tenant tenant) {
		processAll(triggerSource.findAll(tenant.getId()));
	}
	
	private void disableByTenant(Tenant tenant) {
		disableAll(triggerSource.findAll(tenant.getId()));
	}
	
	private void processByAction(Action action) {
		final Set<Trigger> triggers = triggerSource.findAll(action.getTenantId()).stream()
			.filter(trigger -> trigger.getActionIds().contains(action.getId()))
			.collect(Collectors.toSet());
		processAll(triggers);
	}
}
