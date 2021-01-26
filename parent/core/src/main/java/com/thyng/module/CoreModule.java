package com.thyng.module;

import com.thyng.Context;
import com.thyng.repository.MetricRepository;
import com.thyng.service.MetricService;
import com.thyng.util.Constant;

import lombok.Getter;
import lombok.Setter;

public class CoreModule implements Module {
	
	@Setter private Context context;
	@Getter private int order = Constant.ORDER_MODULE_CORE;
	
	@Override
	public void start() throws Exception {
		final MetricRepository metricRepository = context.getMetricRepository();
		if(null != metricRepository) {
			context.setMetricService(new MetricService(metricRepository));
		}
	}

}
