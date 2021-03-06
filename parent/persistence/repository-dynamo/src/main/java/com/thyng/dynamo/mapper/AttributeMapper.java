package com.thyng.dynamo.mapper;

import com.thyng.domain.model.Attribute;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttributeMapper implements Mapper<Attribute, String> {
	
	@NonNull private final String delimiter;

	@Override
	public Attribute map(String text) {
		if(Strings.isBlank(text)) return null;
		final String[] tokens = text.split(delimiter);
		return Attribute.builder()
				.id(tokens[0])
				.name(tokens[1])
				.value(tokens[2])
				.build();
	}

	@Override
	public String unmap(Attribute item) {
		if(null == item) return null;
		return String.join(delimiter, item.getId(), item.getName(), item.getValue());
	}

	
}
	