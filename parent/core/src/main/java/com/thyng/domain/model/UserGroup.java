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
public class UserGroup implements TenantAwareModel<UserGroup> {
	private static final long serialVersionUID = 4533362266165981673L;

	private final String id;
	private final String name;
	private final String script;
	private final String tenantId;
	private final Language language;

}
