package com.thyng.web;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.thyng.Context;
import com.thyng.domain.enumeration.DataType;
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
			return Tenant.builder()
					.id(index.toString())
					.name("Tenant" + index)
					.enabled(0 == index % 2)
					.build();
		}).collect(Collectors.toList());
	}
	
	private void createTemplates(Tenant tenant) {
		final List<Template> templates = context.getTemplateRepository().findAll(tenant.getId());
		if(templates.isEmpty()) {
			templates(tenant).forEach(template -> {
				final Template entity = context.getTemplateRepository().save(template); 
				createThings(entity);
			});
		}
	}
	
	private List<Template> templates(Tenant tenant){
		return IntStream.range(0, 10).boxed().map(index -> {
			return Template.builder()
					.name("Template-"+index)
					.inactivityPeriod(60l + index)
					.tenantId(tenant.getId())
					.sensors(sensors())
					.actuators(actuators())
					.attributes(Collections.unmodifiableSet(attributes()))
					.build();
		}).collect(Collectors.toList());
	}
	
	private Set<Attribute> attributes(){
		final Set<Attribute> attributes = new HashSet<>();
		attributes.add(attribute("floor", "ground"));
		attributes.add(attribute("city", "pune"));
		attributes.add(attribute("department", "PUR"));
		return attributes;
	}
	
	private Attribute attribute(String name, String value) {
		return Attribute.builder()
				.id(null)
				.name(name)
				.value(value)
				.build();
	}
	
	private Set<Sensor> sensors(){
		return IntStream.range(0, 10).boxed().map(index -> {
			return Sensor.builder()
					.name("Sensor" + index)
					.unit("cm")
					.dataType(DataType.NUMBER)
					.build();
		}).collect(Collectors.toSet());
	}
	
	private Set<Actuator> actuators(){
		return IntStream.range(0, 10).boxed().map(index -> {
			return Actuator.builder()
					.name("Actuator-"+index)
					.build();
		}).collect(Collectors.toSet());
	}
	
	private void createThings(Template template) {
		things(template).forEach(context.getThingRepository()::save);
	}
	
	private List<Thing> things(Template template){
		return IntStream.range(0, 10).boxed().map(index -> {
			return Thing.builder()
					.tenantId(template.getTenantId())
					.templateId(template.getId())
					.template(template)
					.name("Thing" + index)
					.inactivityPeriod(300l + index)
					.attributes(Collections.unmodifiableSet(attributes()))
					.build();
		}).collect(Collectors.toList());
	}
	
}
