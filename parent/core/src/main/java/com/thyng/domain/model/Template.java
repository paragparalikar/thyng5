package com.thyng.domain.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With 
@Value
@Builder
@Jacksonized
public class Template implements TenantAwareModel<Template> {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final String tenantId;
	private final String name;
	private final Long inactivityPeriod;
	
	@Builder.Default private final Set<Sensor> sensors = Collections.emptySet();
	@Builder.Default private final Set<Actuator> actuators = Collections.emptySet();
	@Builder.Default private final Set<Attribute> attributes = Collections.emptySet();
	
	public Sensor sensor(String id) {
		return sensors.stream()
				.filter(sensor -> Objects.equals(id, sensor.getId()))
				.findFirst()
				.orElse(null);
	}
}
