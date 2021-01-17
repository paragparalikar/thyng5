package com.thyng.web;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Context;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Module;
import com.thyng.domain.model.Actuator;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Sensor;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.Trigger;
import com.thyng.util.Names;

import spark.Spark;

public class ThyngWebApplication {
	
	private static List<Module> modules() {
		final List<Module> modules = new LinkedList<>();
		ServiceLoader.load(Module.class).forEach(modules::add);
		modules.sort(Comparator.naturalOrder());
		return modules;
	}
	
	private static void start(Context context, List<Module> modules) throws Exception {
		for(Module module : modules) {
			module.setContext(context);
			module.start();
		}
		context.start();
		new DevDataLoader(context).run();
	}
	
	private static void stopOnShutdown(Context context, List<Module> modules) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					modules.sort(Comparator.reverseOrder());
					for(Module module : modules) {
						module.stop();
					}
					context.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) throws Exception {
		final Context context = new Context();
		final List<Module> modules = modules();
		final ObjectMapper objectMapper = new ObjectMapper();
		 
		start(context, modules);
		stopOnShutdown(context, modules);

		Spark.defaultResponseTransformer(objectMapper::writeValueAsString);
		final List<Lifecycle> controllers = Arrays.asList(
				new Controller<Tenant>("/" + Names.TENANT, objectMapper, context.getTenantRepository()),
				new TenantAwareController<Gateway>("/" + Names.GATEWAY, objectMapper, context.getGatewayRepository()),
				new TenantAwareController<Template>("/" + Names.TEMPALTE, objectMapper, context.getTemplateRepository()),
				new TenantAwareController<Sensor>("/" + Names.SENSOR, objectMapper, context.getSensorRepository()),
				new TenantAwareController<Actuator>("/" + Names.ACTUATOR, objectMapper, context.getActuatorRepository()),
				new TenantAwareController<Thing>("/" + Names.THING, objectMapper, context.getThingRepository()),
				new TenantAwareController<Trigger>("/" + Names.TRIGGER, objectMapper, context.getTriggerRepository()));
		
		for(Lifecycle controller : controllers) controller.start();
	}
	
}
