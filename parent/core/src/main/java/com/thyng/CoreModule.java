package com.thyng;

import com.thyng.domain.intf.Module;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Thing;
import com.thyng.repository.MetricRepository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.service.CacheService;
import com.thyng.service.MetricService;

import lombok.Getter;
import lombok.Setter;

public class CoreModule implements Module {
	
	@Setter private Context context;
	@Getter private int order = Integer.MAX_VALUE;
	
	@Override
	public void start() throws Exception {
		final MetricRepository metricRepository = context.getMetricRepository();
		final TenantAwareRepository<Thing> thingRepository = context.getThingRepository();
		final TenantAwareRepository<Template> templateRepository = context.getTemplateRepository();
		context.setCacheService(new CacheService(thingRepository, templateRepository));
		context.setMetricService(new MetricService(metricRepository));
	}

}
