package com.thyng.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.thyng.Context;
import com.thyng.domain.model.Actuator;
import com.thyng.domain.model.Attribute;
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
		if(context.getTenantRepository().findAll().isEmpty()) {
			tenants().forEach(tenant -> {
				final Tenant entity = context.getTenantRepository().save(tenant);
				createTemplates(entity);
			});	
		}
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
		final List<Template> templates = context.getTemplateRepository().findAll(tenant.getId());
		if(templates.isEmpty()) {
			templates(tenant).forEach(template -> {
				template.setSensors(sensors(template));
				template.setActuators(actuators(template));
				final Template entity = context.getTemplateRepository().save(template); 
				createThings(entity);
			});
		}
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
	
	private Set<Attribute> attributes(){
		final Set<Attribute> attributes = new HashSet<>();
		attributes.add(new Attribute(null, "floor", "ground"));
		attributes.add(new Attribute(null, "city", "pune"));
		attributes.add(new Attribute(null, "department", "PUR"));
		return attributes;
	}
	
	private Set<Sensor> sensors(Template template){
		return IntStream.range(0, 10).boxed().map(index -> {
			final Sensor sensor = new Sensor();
			sensor.setName("Sensor-"+index);
			sensor.setUnit("cm");
			return sensor;
		}).collect(Collectors.toSet());
	}
	
	private Set<Actuator> actuators(Template template){
		return IntStream.range(0, 10).boxed().map(index -> {
			final Actuator actuator = new Actuator();
			actuator.setName("Actuator-"+index);
			return actuator;
		}).collect(Collectors.toSet());
	}
	
	private void createThings(Template template) {
		things(template).forEach(context.getThingRepository()::save);
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
