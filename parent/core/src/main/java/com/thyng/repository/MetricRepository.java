package com.thyng.repository;

import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Metric;

public interface MetricRepository extends Lifecycle {
	
	void create(Metric metric, Callback<Metric> callback);

}
