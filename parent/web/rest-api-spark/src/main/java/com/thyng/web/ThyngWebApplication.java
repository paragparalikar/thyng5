package com.thyng.web;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.Context;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Module;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.Trigger;
import com.thyng.util.Names;

import spark.Filter;
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
	
	private static void cors() {
		Spark.options("/*",
		        (request, response) -> {
		            final String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
		            if (accessControlRequestHeaders != null) response.header("Access-Control-Allow-Headers",accessControlRequestHeaders);

		            final String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
		            if (accessControlRequestMethod != null) response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
		            return "OK";
		        });
		Spark.after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "*");
        });
	}

	public static void main(String[] args) throws Exception {
		final Context context = new Context();
		final List<Module> modules = modules();
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		 
		start(context, modules);
		stopOnShutdown(context, modules);

		cors();
		Spark.defaultResponseTransformer(objectMapper::writeValueAsString);
		final List<Lifecycle> controllers = Arrays.asList(
				new Controller<Tenant>("/" + Names.TENANT, Tenant.class, objectMapper, context.getTenantRepository()),
				new TenantAwareController<Gateway>("/" + Names.GATEWAY, Gateway.class, objectMapper, context.getGatewayRepository()),
				new TenantAwareController<Template>("/" + Names.TEMPALTE, Template.class, objectMapper, context.getTemplateRepository()),
				new TenantAwareController<Thing>("/" + Names.THING, Thing.class, objectMapper, context.getThingRepository()),
				new TenantAwareController<ThingGroup>("/" + Names.THING_GROUP, ThingGroup.class, objectMapper, context.getThingGroupRepository()),
				new TenantAwareController<Trigger>("/" + Names.TRIGGER, Trigger.class, objectMapper, context.getTriggerRepository()));
		
		for(Lifecycle controller : controllers) controller.start();
	}
	
}
