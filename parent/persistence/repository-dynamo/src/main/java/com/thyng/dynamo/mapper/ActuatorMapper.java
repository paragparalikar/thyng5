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
		if(null == item) return "";
		return String.join(delimiter, item.getId(), item.getName());
	}

	@Override
	public Actuator map(String text) {
		if(Strings.isBlank(text)) return null;
		final Actuator item = new Actuator();
		final String[] tokens = text.split(delimiter);
		item.setId(tokens[0]);
		item.setName(tokens[1]);
		return item;
	}
}
