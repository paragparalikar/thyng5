package com.thyng.domain.dto;

import java.io.Serializable;

import com.thyng.domain.enumeration.DataType;
import com.thyng.domain.model.Sensor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorDto implements Serializable {
	private static final long serialVersionUID = 7872158215269334381L;

	public static SensorDto from(Sensor sensor, Object value) {
		return SensorDto.builder()
				.value(value)
				.id(sensor.getId())
				.name(sensor.getName())
				.unit(sensor.getUnit())
				.dataType(sensor.getDataType())
				.build();
	}
	
	private final String id;
	private final DataType dataType;
	private final String name;
	private final String unit;
	private final Object value;
}
