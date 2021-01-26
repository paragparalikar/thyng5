package com.thyng.domain.model;

import java.util.Collections;
import java.util.Set;

import com.thyng.domain.enumeration.EventType;
import com.thyng.domain.enumeration.Language;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class Trigger implements TenantAwareModel<Trigger> {
	private static final long serialVersionUID = 1L;

	private final String id;
	private final Window window;
	private final String script;
	private final String tenantId;
	private final Language language;
	private final EventType eventType;
	private final String name;
	@Builder.Default private final Boolean enabled = Boolean.TRUE;
	@Builder.Default private final Set<String> actionIds = Collections.emptySet();
	@Builder.Default private final Set<String> thingGroupIds = Collections.emptySet();
	@Builder.Default private final Boolean includeEvents = Boolean.FALSE;
	@Builder.Default private final Boolean includeAggregations = Boolean.TRUE;

}
