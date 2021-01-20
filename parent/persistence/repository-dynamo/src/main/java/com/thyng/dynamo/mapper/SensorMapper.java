package com.thyng.dynamo.mapper;

import com.thyng.domain.enumeration.DataType;
import com.thyng.domain.model.Sensor;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SensorMapper implements Mapper<Sensor, String> {
	
	@NonNull private final String delimiter;

	@Override
	public String unmap(Sensor item) {
		if(null == item) return null;
		return String.join(delimiter, item.getId(), item.getName(), item.getUnit(), item.getDataType().name());
	}

	@Override
	public Sensor map(String text) {
		if(Strings.isBlank(text)) return null;
		final Sensor item = new Sensor();
		final String[] tokens = text.split(delimiter);
		item.setId(tokens[0]);
		item.setName(tokens[1]);
		item.setUnit(tokens[2]);
		item.setDataType(DataType.valueOf(tokens[3]));
		return item;
	}

}
