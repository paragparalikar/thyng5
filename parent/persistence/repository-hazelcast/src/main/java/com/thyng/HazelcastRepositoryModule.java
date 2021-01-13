package com.thyng;

import java.util.Optional;

import com.hazelcast.core.HazelcastInstance;
import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Module;
import com.thyng.domain.intf.Nameable;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Actuator;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Sensor;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.Trigger;
import com.thyng.repository.MultiTenantRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.hazelcast.HazelcastRepository;
import com.thyng.repository.hazelcast.MultiTenantHazelcastRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class HazelcastRepositoryModule implements Module {

	@Setter private Context context;
	@Getter private final int order = 1;
	
	@Override
	public void start() throws Exception {
		final HazelcastInstance hazelcastInstance = context.getHazelcastInstance();
		context.setTenantRepository(wrap(Tenant.CACHE_NAME, context.getTenantRepository(), hazelcastInstance));
		context.setGatewayRepository(wrap(Gateway.CACHE_NAME, context.getGatewayRepository(), hazelcastInstance));
		context.setTemplateRepository(wrap(Template.CACHE_NAME, context.getTemplateRepository(), hazelcastInstance));
		context.setSensorRepository(wrap(Sensor.CACHE_NAME, context.getSensorRepository(), hazelcastInstance));
		context.setActuatorRepository(wrap(Actuator.CACHE_NAME, context.getActuatorRepository(), hazelcastInstance));
		context.setThingRepository(wrap(Thing.CACHE_NAME, context.getThingRepository(), hazelcastInstance));
		context.setTriggerRepository(wrap(Trigger.CACHE_NAME, context.getTriggerRepository(), hazelcastInstance));
		context.setActionRepository(wrap(Action.CACHE_NAME, context.getActionRepository(), hazelcastInstance));
		context.setAlertActionRepository(wrap(Action.CACHE_NAME, context.getAlertActionRepository(), hazelcastInstance));
		context.setMailAlertActionRepository(wrap(Action.CACHE_NAME, context.getMailAlertActionRepository(), hazelcastInstance));
	}
	
	private <T extends Identifiable<String> & Nameable> Repository<T, String> wrap(
			String cacheName, Repository<T, String> delegate, HazelcastInstance hazelcastInstance){
		return Optional.ofNullable(delegate)
				.map(value -> new HazelcastRepository<>(cacheName, delegate, hazelcastInstance))
				.orElse(null);
	}
	
	private <T extends TenantAwareModel> MultiTenantRepository<T> wrap(
			String cacheName, MultiTenantRepository<T> delegate, HazelcastInstance hazelcastInstance){
		return Optional.ofNullable(delegate)
				.map(value -> new MultiTenantHazelcastRepository<>(cacheName, delegate, hazelcastInstance))
				.orElse(null);
	}

}
