package com.thyng.domain.model;

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
public class ThingGroup implements TenantAwareModel<ThingGroup> {
	private static final long serialVersionUID = -8917862417622815872L;

	private final String id;
	private final String tenantId;
	private final String script;
	private final Language language;
	private final String name;
}
