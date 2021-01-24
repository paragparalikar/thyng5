package com.thyng.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.dto.MetricDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metric implements Serializable {
	private static final long serialVersionUID = -8066304876436020179L;
	
	private Thing thing;
	private Template template;
	private MetricDto metricDto;
	private final Map<Sensor, Object> sensorValues = new HashMap<>();
	
}
