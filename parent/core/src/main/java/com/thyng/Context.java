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
import com.thyng.repository.MetricRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.service.CacheService;
import com.thyng.service.MetricService;

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
	private TenantAwareRepository<Trigger> triggerRepository;
	private TenantAwareRepository<Action> actionRepository;
	private TenantAwareRepository<User> userRepository;
	private TenantAwareRepository<UserGroup> userGroupRepository;
	
	private CacheService cacheService;
	private MetricService metricService;
	
	@Override
	public void start() throws Exception {
		if(null != counterRepository) counterRepository.start();
		if(null != tenantRepository) tenantRepository.start();
		if(null != gatewayRepository) gatewayRepository.start();
		if(null != templateRepository) templateRepository.start();
		if(null != thingRepository) thingRepository.start();
		if(null != thingGroupRepository) thingGroupRepository.start();
		if(null != triggerRepository) triggerRepository.start();
		if(null != actionRepository) actionRepository.start();
		if(null != userRepository) userRepository.start();
		if(null != userGroupRepository) userGroupRepository.start();
		if(null != metricRepository) metricRepository.start();
		
		cacheService.start();
		metricService.start();
	}
	
	@Override
	public void stop() throws Exception {
		metricService.stop();
		cacheService.stop();
		
		if(null != metricRepository) metricRepository.stop();
		if(null != actionRepository) actionRepository.stop();
		if(null != triggerRepository) triggerRepository.stop();
		if(null != thingGroupRepository) thingGroupRepository.stop();
		if(null != thingRepository) thingRepository.stop();
		if(null != templateRepository) templateRepository.stop();
		if(null != gatewayRepository) gatewayRepository.stop();
		if(null != userGroupRepository) userGroupRepository.stop();
		if(null != userRepository) userRepository.stop();
		if(null != tenantRepository) tenantRepository.stop();
		if(null != counterRepository) counterRepository.stop();
	}

}
