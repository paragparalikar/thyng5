package com.thyng.map;

import java.util.HashMap;
import java.util.Map;

import com.thyng.Callback;
import com.thyng.domain.model.Metric;
import com.thyng.repository.MetricRepository;

public class MapMetricRepository implements MetricRepository {
	
	private final Map<String, Metric> cache = new HashMap<>();

	@Override
	public void create(Metric metric, Callback<Metric> callback) {
		cache.put(metric.getThing().getId(), metric);
		if(null != callback) callback.call(metric, null);
	}

}
