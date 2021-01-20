package com.thyng;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.Trigger;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TenantAwareRepository;

import lombok.Data;

@Data
public class Context implements Lifecycle {

	private CounterRepository counterRepository;
	private Repository<Tenant, String> tenantRepository;
	private TenantAwareRepository<Gateway> gatewayRepository;
	private TenantAwareRepository<Template> templateRepository;
	private TenantAwareRepository<Thing> thingRepository;
	private TenantAwareRepository<ThingGroup> thingGroupRepository;
	private TenantAwareRepository<Trigger> triggerRepository;
	private TenantAwareRepository<Action> actionRepository;
	
	@Override
	public void start() throws Exception {
		counterRepository.start();
		tenantRepository.start();
		gatewayRepository.start();
		templateRepository.start();
		thingRepository.start();
		thingGroupRepository.start();
		triggerRepository.start();
	}
	
	@Override
	public void stop() throws Exception {
		triggerRepository.stop();
		thingGroupRepository.stop();
		thingRepository.stop();
		templateRepository.stop();
		gatewayRepository.stop();
		tenantRepository.stop();
		counterRepository.stop();
	}


}
