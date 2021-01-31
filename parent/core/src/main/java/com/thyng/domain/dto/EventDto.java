package com.thyng.domain.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.thyng.domain.enumeration.EventType;
import com.thyng.domain.model.Metric;
import com.thyng.domain.model.Template;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto implements Serializable {
	private static final long serialVersionUID = 1493969783124829429L;
	
	public static EventDto from(Metric metric) {
		final List<SensorDto> sensors = metric.getTemplate().getSensors().stream()
				.map(sensor -> SensorDto.from(sensor, metric.getSensorValues().get(sensor)))
				.collect(Collectors.toList());
		final Map<String, SensorDto> sensorsByName = sensors.stream()
				.collect(Collectors.toMap(SensorDto::getName, Function.identity()));
		final Map<String, SensorDto> sensorsById = sensors.stream()
				.collect(Collectors.toMap(SensorDto::getId, Function.identity()));
		return EventDto.builder()
				.sensors(sensors)
				.sensorsById(sensorsById)
				.sensorsByName(sensorsByName)
				.template(metric.getTemplate())
				.thing(ThingDto.from(metric.getThing(), metric.getTemplate()))
				.build();
	}

	private final ThingDto thing;
	private final EventType type;
	private final Template template;
	@Builder.Default private final List<SensorDto> sensors = Collections.emptyList();
	@Builder.Default private final Map<String, SensorDto> sensorsById = Collections.emptyMap();
	@Builder.Default private final Map<String, SensorDto> sensorsByName = Collections.emptyMap();
	
}
