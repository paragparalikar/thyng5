package com.thyng.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

import com.thyng.domain.sensor.Sensor;
import com.thyng.domain.sensor.SensorRepository;
import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantRepository;
import com.thyng.domain.thing.Thing;
import com.thyng.domain.thing.ThingRepository;
import com.thyng.domain.thing.ThingStatus;

import lombok.RequiredArgsConstructor;

//@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataSetupRunner implements CommandLineRunner {

	private final ThingRepository thingRepository;
	private final TenantRepository tenantRepository;
	private final SensorRepository sensorRepository;

	@Override
	public void run(String... args) throws Exception {
		thingRepository.findAll().stream()
			.map(this::sensors)
			.flatMap(Collection::stream)
			.forEach(sensorRepository::save);
	}
	
	private List<Sensor> sensors(final Thing thing){
		final List<Sensor> sensors = new ArrayList<>(5);
		for(int index = 0; index < 10; index++) {
			final Sensor sensor = new Sensor();
			sensor.setName("Sensor-"+index);
			sensor.setTenantId(thing.getTenantId());
			sensor.setThingId(thing.getId());
			sensor.setUnit("mm");
			sensors.add(sensor);
		}
		return sensors;
	}
	
	private List<Thing> things(final Tenant tenant){
		final List<Thing> things = new ArrayList<>(10);
		for(int index = 0; index < 10; index++) {
			final Thing thing = new Thing();
			thing.setName("Thing-" + index);
			thing.setTenantId(tenant.getId());
			thing.setInactivityPeriod(60l + index);
			thing.setAttributes(attributes());
			thing.setStatus(0 == index % 2 ? ThingStatus.ONLINE : ThingStatus.OFFLINE);
			things.add(thing);
		}
		return things;
	}
	
	private Map<String, String> attributes(){
		final Map<String, String> attributes = new HashMap<>();
		attributes.put("color", "red");
		attributes.put("make", "m1");
		attributes.put("model", "m2");
		attributes.put("department", "dep1");
		return attributes;
	}

	private List<Tenant> tenants(){
		final List<Tenant> tenants = new ArrayList<>(10);
		for(int index = 0; index < 10; index++) {
			final Tenant tenant = new Tenant();
			tenant.setName("Tenant_" + index);
			tenants.add(tenant);
		}
		return tenants;
	}
	
}
