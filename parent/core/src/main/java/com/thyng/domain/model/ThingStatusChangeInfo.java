package com.thyng.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

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
public class ThingStatusChangeInfo implements Serializable, Identifiable<ThingStatusChangeInfo> {
	private static final long serialVersionUID = 2661274618216122303L;
	
	@NonNull private final String id;
	@Builder.Default private final Map<String, ThingStatusChange> changes = Collections.emptyMap();
	
}
