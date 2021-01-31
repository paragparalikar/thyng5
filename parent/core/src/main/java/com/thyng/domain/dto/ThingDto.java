package com.thyng.domain.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import com.thyng.domain.model.Attribute;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Thing;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThingDto implements Serializable {
	private static final long serialVersionUID = 214383779449045386L;
	
	public static ThingDto from(Thing thing, Template template) {
		if(!Objects.equals(thing.getTemplateId(), template.getId())) {
			final String message = String.format("Thing %s(%s) does not belong to template %s(%s)", 
					thing.getName(), thing.getId(), template.getName(), template.getId());
			throw new IllegalArgumentException(message);
		}
		return ThingDto.builder()
				.id(thing.getId())
				.name(thing.getName())
				.template(template)
				.attributes(thing.getAttributes())
				.inactivityPeriod(thing.getInactivityPeriod())
				.build();
	}
	
	private final String id;
	private final String name;
	private final Template template;
	private final Long inactivityPeriod;
	@Builder.Default private final Set<Attribute> attributes = Collections.emptySet();
	
}
