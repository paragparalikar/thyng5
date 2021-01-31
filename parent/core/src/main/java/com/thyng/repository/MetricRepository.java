package com.thyng.repository;

import java.util.Map;

import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Metric;

public interface MetricRepository extends Lifecycle {
	
	Map<String, Long> findAllLatestTimestamps();
	
	void create(Metric metric, Callback<Metric> callback);
	
}
