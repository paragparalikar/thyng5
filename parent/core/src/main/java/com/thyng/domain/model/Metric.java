package com.thyng.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.thyng.domain.dto.MetricDto;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class Metric implements Serializable {
	private static final long serialVersionUID = -8066304876436020179L;
	
	private final Thing thing;
	private final Template template;
	private final MetricDto metricDto;
	@Builder.Default private final Map<Sensor, Object> sensorValues = Collections.emptyMap();
	
}
