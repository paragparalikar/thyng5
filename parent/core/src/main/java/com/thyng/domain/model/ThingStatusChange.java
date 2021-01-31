package com.thyng.domain.model;

import java.io.Serializable;

import com.thyng.domain.intf.Identifiable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class ThingStatusChange implements Serializable, Identifiable<ThingStatusChange> {
	private static final long serialVersionUID = 3080311318883126025L;

	@NonNull private final String id;
	@NonNull private final Boolean online;
	@NonNull private final Long timestamp;
	
}
