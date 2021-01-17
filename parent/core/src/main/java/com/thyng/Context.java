package com.thyng;

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
import com.thyng.repository.CounterRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TemplateAwareRepository;
import com.thyng.repository.TenantAwareRepository;

import lombok.Data;

@Data
public class Context implements Lifecycle {

	private CounterRepository counterRepository;
	private Repository<Tenant, String> tenantRepository;
	private TenantAwareRepository<Gateway> gatewayRepository;
	private TenantAwareRepository<Template> templateRepository;
	private TemplateAwareRepository<Sensor> sensorRepository;
	private TemplateAwareRepository<Actuator> actuatorRepository;
	private TemplateAwareRepository<Thing> thingRepository;
	private TenantAwareRepository<Trigger> triggerRepository;
	private TenantAwareRepository<Action> actionRepository;
	private TenantAwareRepository<AlertAction> alertActionRepository;
	private TenantAwareRepository<MailAlertAction> mailAlertActionRepository;
	
	@Override
	public void start() throws Exception {
		counterRepository.start();
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
		triggerRepository.stop();
		thingRepository.stop();
		sensorRepository.stop();
		actuatorRepository.stop();
		templateRepository.stop();
		gatewayRepository.stop();
		tenantRepository.stop();
		counterRepository.stop();
	}


}
