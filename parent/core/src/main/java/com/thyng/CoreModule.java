package com.thyng;

import com.thyng.domain.intf.Module;
import com.thyng.repository.MetricRepository;
import com.thyng.service.MetricService;

import lombok.Getter;
import lombok.Setter;

public class CoreModule implements Module {
	
	@Setter private Context context;
	@Getter private int order = Integer.MAX_VALUE;
	
	@Override
	public void start() throws Exception {
		final MetricRepository metricRepository = context.getMetricRepository();
		context.setMetricService(new MetricService(metricRepository));
	}

}
