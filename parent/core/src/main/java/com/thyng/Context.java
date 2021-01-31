package com.thyng;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.ThingInfo;
import com.thyng.domain.model.ThingStatusChangeInfo;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.TriggerEvaluationInfo;
import com.thyng.domain.model.TriggerInfo;
import com.thyng.domain.model.User;
import com.thyng.domain.model.UserGroup;
import com.thyng.event.EventBus;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.MappingRepository;
import com.thyng.repository.MetricRepository;
import com.thyng.repository.NameableRepository;
import com.thyng.repository.ObjectRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.service.MetricService;
import com.thyng.service.evaluation.trigger.TriggerEvaluationEligibilityService;
import com.thyng.service.mapping.ThingGroupMappingService;
import com.thyng.service.mapping.UserGroupMappingService;

import lombok.Data;

@Data
public class Context implements Lifecycle {

	private MetricRepository metricRepository;
	private CounterRepository counterRepository;
	private NameableRepository<Tenant> tenantRepository;
	private TenantAwareRepository<Gateway> gatewayRepository;
	private TenantAwareRepository<Template> templateRepository;
	private TenantAwareRepository<Thing> thingRepository;
	private Repository<ThingInfo> thingInfoRepository;
	private TenantAwareRepository<ThingGroup> thingGroupRepository;
	private MappingRepository thingGroupMappingRepository;
	private TenantAwareRepository<Trigger> triggerRepository;
	private Repository<TriggerInfo> triggerInfoRepository;
	private TenantAwareRepository<Action> actionRepository;
	private TenantAwareRepository<User> userRepository;
	private TenantAwareRepository<UserGroup> userGroupRepository;
	private MappingRepository userGroupMappingRepository;
	private ObjectRepository<TriggerEvaluationInfo> triggerEvaluationInfoRepository;
	private ObjectRepository<ThingStatusChangeInfo> thingStatusChangeInfoRepository;
	
	private EventBus eventBus;
	private MetricService metricService;
	private UserGroupMappingService userGroupMappingService;
	private ThingGroupMappingService thingGroupMappingService;
	private TriggerEvaluationEligibilityService triggerEvaluationEligibilityService;
	
	@Override
	public void start() throws Exception {
		if(null != eventBus) eventBus.start();
		
		if(null != counterRepository) counterRepository.start();
		if(null != tenantRepository) tenantRepository.start();
		if(null != gatewayRepository) gatewayRepository.start();
		if(null != templateRepository) templateRepository.start();
		if(null != thingRepository) thingRepository.start();
		if(null != thingInfoRepository) thingInfoRepository.start();
		if(null != thingGroupRepository) thingGroupRepository.start();
		if(null != thingGroupMappingRepository) thingGroupMappingRepository.start();
		if(null != triggerRepository) triggerRepository.start();
		if(null != triggerInfoRepository) triggerInfoRepository.start();
		if(null != actionRepository) actionRepository.start();
		if(null != userRepository) userRepository.start();
		if(null != userGroupRepository) userGroupRepository.start();
		if(null != userGroupMappingRepository) userGroupMappingRepository.start();
		if(null != metricRepository) metricRepository.start();
		if(null != triggerEvaluationInfoRepository) triggerEvaluationInfoRepository.start();
		
		if(null != metricService) metricService.start();
		if(null != userGroupMappingService) userGroupMappingService.start();
		if(null != thingGroupMappingService) thingGroupMappingService.start();
		if(null != triggerEvaluationEligibilityService) triggerEvaluationEligibilityService.start();
		if(null != thingStatusChangeInfoRepository) thingStatusChangeInfoRepository.start();
	}
	
	@Override
	public void stop() throws Exception {
		if(null != thingStatusChangeInfoRepository) thingStatusChangeInfoRepository.stop();
		if(null != triggerEvaluationEligibilityService) triggerEvaluationEligibilityService.stop();
		if(null != metricService) metricService.stop();
		if(null != userGroupMappingService) userGroupMappingService.stop();
		if(null != thingGroupMappingService) thingGroupMappingService.stop();
		
		if(null != metricRepository) metricRepository.stop();
		if(null != actionRepository) actionRepository.stop();
		if(null != triggerRepository) triggerRepository.stop();
		if(null != triggerInfoRepository) triggerInfoRepository.stop();
		if(null != thingGroupRepository) thingGroupRepository.stop();
		if(null != thingGroupMappingRepository) thingGroupMappingRepository.stop();
		if(null != thingRepository) thingRepository.stop();
		if(null != thingInfoRepository) thingInfoRepository.stop();
		if(null != templateRepository) templateRepository.stop();
		if(null != gatewayRepository) gatewayRepository.stop();
		if(null != userGroupRepository) userGroupRepository.stop();
		if(null != userGroupMappingRepository) userGroupMappingRepository.stop();
		if(null != userRepository) userRepository.stop();
		if(null != tenantRepository) tenantRepository.stop();
		if(null != counterRepository) counterRepository.stop();
		if(null != triggerEvaluationInfoRepository) triggerEvaluationInfoRepository.stop();
		
		if(null != eventBus) eventBus.stop();
	}

}
