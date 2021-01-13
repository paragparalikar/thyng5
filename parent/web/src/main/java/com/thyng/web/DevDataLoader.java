package com.thyng.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.thyng.Context;
import com.thyng.domain.intf.Callback;
import com.thyng.domain.model.Actuator;
import com.thyng.domain.model.Sensor;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DevDataLoader implements Runnable {

	private final Context context;

	@Override
	public void run() {
		createTenantsIfEmpty();
	}
	
	private void createTenantsIfEmpty() {
		context.getTenantRepository().count(Callback.<Long>builder()
			.success(count -> {
				if(0 == count) createTenants();
			})
			.build());
	}
	
	private void createTenants() {
		tenants().forEach(tenant -> {
			context.getTenantRepository().save(tenant, Callback.<Tenant>builder()
				.success(value -> {
					createTemplates(value);
				})
				.build());
		});
	}
	
	private List<Tenant> tenants(){
		return IntStream.range(0, 10).boxed().map(index -> {
			final Tenant tenant = new Tenant();
			tenant.setId(index.toString());
			tenant.setName("Tenant-"+index);
			tenant.setEnabled(0 == index % 2);
			return tenant;
		}).collect(Collectors.toList());
	}
	
	private void createTemplates(Tenant tenant) {
		templates(tenant).forEach(template -> {
			context.getTemplateRepository().save(template, Callback.<Template>builder()
					.success(value -> {
						createSensors(value);
						createActuators(value);
						createThings(value);
					})
					.build());
		});
	}
	
	private List<Template> templates(Tenant tenant){
		return IntStream.range(0, 10).boxed().map(index -> {
			final Template template = new Template();
			template.setName("Template-"+index);
			template.setAttributes(attributes());
			template.setInactivityPeriod(60 + index);
			template.setTenantId(tenant.getId());
			return template;
		}).collect(Collectors.toList());
	}
	
	private Map<String, String> attributes(){
		final Map<String, String> attributes = new HashMap<>();
		attributes.put("floor", "ground");
		attributes.put("city", "pune");
		attributes.put("department", "PUR");
		return attributes;
	}
	
	private void createSensors(Template template) {
		sensors(template).forEach(sensor -> {
			context.getSensorRepository().save(sensor, Callback.<Sensor>builder().build());
		});
	}
	
	private List<Sensor> sensors(Template template){
		return IntStream.range(0, 10).boxed().map(index -> {
			final Sensor sensor = new Sensor();
			sensor.setTenantId(template.getTenantId());
			sensor.setTemplateId(template.getId());
			sensor.setName("Sensor-"+index);
			sensor.setUnit("cm");
			return sensor;
		}).collect(Collectors.toList());
	}
	
	private void createActuators(Template template) {
		actuators(template).forEach(actuator -> {
			context.getActuatorRepository().save(actuator, Callback.<Actuator>builder().build());
		});
	}
	
	private List<Actuator> actuators(Template template){
		return IntStream.range(0, 10).boxed().map(index -> {
			final Actuator actuator = new Actuator();
			actuator.setTenantId(template.getTenantId());
			actuator.setTemplateId(template.getId());
			actuator.setName("Actuator-"+index);
			return actuator;
		}).collect(Collectors.toList());
	}
	
	private void createThings(Template template) {
		things(template).forEach(thing -> {
			context.getThingRepository().save(thing, Callback.<Thing>builder().build());
		});
	}
	
	private List<Thing> things(Template template){
		return IntStream.range(0, 10).boxed().map(index -> {
			final Thing thing = new Thing();
			thing.setTenantId(template.getTenantId());
			thing.setTemplateId(template.getId());
			thing.setName("Thing-"+index);
			thing.setAttributes(attributes());
			return thing;
		}).collect(Collectors.toList());
	}
	
}
