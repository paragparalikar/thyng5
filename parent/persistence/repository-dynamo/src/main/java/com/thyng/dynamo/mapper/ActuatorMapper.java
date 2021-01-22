package com.thyng.dynamo.mapper;

import com.thyng.domain.model.Actuator;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActuatorMapper implements Mapper<Actuator, String> {

	@NonNull private final String delimiter;
	
	@Override
	public String unmap(Actuator item) {
		if(null == item) return null;
		return String.join(delimiter, item.getId(), item.getName());
	}

	@Override
	public Actuator map(String text) {
		if(Strings.isBlank(text)) return null;
		final String[] tokens = text.split(delimiter);
		return new Actuator(tokens[0], tokens[1]);
	}
}
