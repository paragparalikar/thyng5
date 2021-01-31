package com.thyng.dynamo.mapper;

import com.thyng.domain.model.ThingStatusChange;
import com.thyng.util.Dates;
import com.thyng.util.Strings;

public class ThingStatusChangeMapper implements Mapper<ThingStatusChange, String> {

	@Override
	public ThingStatusChange map(String text) {
		if(Strings.isBlank(text)) return null;
		final String[] tokens = text.split(",");
		return ThingStatusChange.builder()
				.id(tokens[0])
				.online("Y".equals(tokens[2]))
				.timestamp(Dates.decodeBySeconds(tokens[3]))
				.build();
	}

	@Override
	public String unmap(ThingStatusChange entity) {
		return null == entity ? null : 
			String.join(",", entity.getId(), entity.getOnline() ? "Y" : "N", 
					Dates.encodeToSeconds(entity.getTimestamp()));
	}

}
