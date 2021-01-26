package com.thyng.domain.model;

import java.util.Collections;
import java.util.Set;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class Thing implements TenantAwareModel<Thing> {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final String name;
	private final String tenantId;
	private final String templateId;
	private final Long inactivityPeriod;
	@Builder.Default private final Set<Attribute> attributes = Collections.emptySet();
	
	private final transient Template template;

}
