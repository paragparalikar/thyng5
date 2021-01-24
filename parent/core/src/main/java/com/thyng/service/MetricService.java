package com.thyng.service;

import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Metric;
import com.thyng.repository.MetricRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MetricService implements Lifecycle {
	
	@NonNull private final MetricRepository metricRepository;
	
	public void create(@NonNull Metric metric, Callback<Metric> callback) {
		metricRepository.create(metric, callback);
	}
	
}
 