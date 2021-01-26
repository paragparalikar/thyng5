package com.thyng;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.User;
import com.thyng.domain.model.UserGroup;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.MappingRepository;
import com.thyng.repository.MetricRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.repository.TriggerInfoRepository;
import com.thyng.service.EventService;
import com.thyng.service.MetricService;
import com.thyng.service.ThingGroupMappingService;
import com.thyng.service.UserGroupMappingService;

import lombok.Data;

@Data
public class Context implements Lifecycle {

	private MetricRepository metricRepository;
	private CounterRepository counterRepository;
	private Repository<Tenant, String> tenantRepository;
	private TenantAwareRepository<Gateway> gatewayRepository;
	private TenantAwareRepository<Template> templateRepository;
	private TenantAwareRepository<Thing> thingRepository;
	private TenantAwareRepository<ThingGroup> thingGroupRepository;
	private MappingRepository thingGroupMappingRepository;
	private TenantAwareRepository<Trigger> triggerRepository;
	private TriggerInfoRepository triggerInfoRepository;
	private TenantAwareRepository<Action> actionRepository;
	private TenantAwareRepository<User> userRepository;
	private TenantAwareRepository<UserGroup> userGroupRepository;
	private MappingRepository userGroupMappingRepository;
	
	private EventService eventService;
	private MetricService metricService;
	private UserGroupMappingService userGroupMappingService;
	private ThingGroupMappingService thingGroupMappingService;
	
	@Override
	public void start() throws Exception {
		if(null != counterRepository) counterRepository.start();
		if(null != tenantRepository) tenantRepository.start();
		if(null != gatewayRepository) gatewayRepository.start();
		if(null != templateRepository) templateRepository.start();
		if(null != thingRepository) thingRepository.start();
		if(null != thingGroupRepository) thingGroupRepository.start();
		if(null != thingGroupMappingRepository) thingGroupMappingRepository.start();
		if(null != triggerRepository) triggerRepository.start();
		if(null != triggerInfoRepository) triggerInfoRepository.start();
		if(null != actionRepository) actionRepository.start();
		if(null != userRepository) userRepository.start();
		if(null != userGroupRepository) userGroupRepository.start();
		if(null != userGroupMappingRepository) userGroupMappingRepository.start();
		if(null != metricRepository) metricRepository.start();
		
		if(null != metricService) metricService.start();
		if(null != userGroupMappingService) userGroupMappingService.start();
		if(null != thingGroupMappingService) thingGroupMappingService.start();
		if(null != eventService) eventService.start();
	}
	
	@Override
	public void stop() throws Exception {
		if(null != eventService) eventService.stop();
		if(null != userGroupMappingService) userGroupMappingService.stop();
		if(null != thingGroupMappingService) thingGroupMappingService.stop();
		if(null != metricService) metricService.stop();
		
		if(null != metricRepository) metricRepository.stop();
		if(null != actionRepository) actionRepository.stop();
		if(null != triggerRepository) triggerRepository.stop();
		if(null != triggerInfoRepository) triggerInfoRepository.stop();
		if(null != thingGroupRepository) thingGroupRepository.stop();
		if(null != thingGroupMappingRepository) thingGroupMappingRepository.stop();
		if(null != thingRepository) thingRepository.stop();
		if(null != templateRepository) templateRepository.stop();
		if(null != gatewayRepository) gatewayRepository.stop();
		if(null != userGroupRepository) userGroupRepository.stop();
		if(null != userGroupMappingRepository) userGroupMappingRepository.stop();
		if(null != userRepository) userRepository.stop();
		if(null != tenantRepository) tenantRepository.stop();
		if(null != counterRepository) counterRepository.stop();
	}

}
