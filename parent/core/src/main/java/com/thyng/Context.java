package com.thyng;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.JetInstance;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Actuator;
import com.thyng.domain.model.AlertAction;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.MailAlertAction;
import com.thyng.domain.model.Sensor;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.Trigger;
import com.thyng.repository.MultiTenantRepository;
import com.thyng.repository.Repository;

import lombok.Data;

@Data
public class Context implements Lifecycle {

	private JetInstance jetInstnace;
	private HazelcastInstance hazelcastInstance;
	private Repository<Tenant, String> tenantRepository;
	private MultiTenantRepository<Gateway> gatewayRepository;
	private MultiTenantRepository<Template> templateRepository;
	private MultiTenantRepository<Sensor> sensorRepository;
	private MultiTenantRepository<Actuator> actuatorRepository;
	private MultiTenantRepository<Thing> thingRepository;
	private MultiTenantRepository<Trigger> triggerRepository;
	private MultiTenantRepository<Action> actionRepository;
	private MultiTenantRepository<AlertAction> alertActionRepository;
	private MultiTenantRepository<MailAlertAction> mailAlertActionRepository;
	
	@Override
	public void start() throws Exception {
		tenantRepository.start();
		gatewayRepository.start();
		templateRepository.start();
		sensorRepository.start();
		actuatorRepository.start();
		thingRepository.start();
		triggerRepository.start();
	}
	
	@Override
	public void stop() throws Exception {
		tenantRepository.stop();
		gatewayRepository.stop();
		templateRepository.stop();
		sensorRepository.stop();
		actuatorRepository.stop();
		thingRepository.stop();
		triggerRepository.stop();
	}

}
