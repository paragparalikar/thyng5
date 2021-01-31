package com.thyng.domain.model;

import java.io.Serializable;

import com.thyng.domain.intf.Identifiable;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class ThingInfo implements Serializable, Identifiable<ThingInfo> {
	private static final long serialVersionUID = -4841182871379677017L;

	private final String id;
	private final Boolean online;
	private final Long lastStatusChangeTimestamp;

}
