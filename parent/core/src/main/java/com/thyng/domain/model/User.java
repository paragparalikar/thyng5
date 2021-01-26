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
public class User implements TenantAwareModel<User> {
	private static final long serialVersionUID = 2764907017660209767L;

	private final String id;
	private final String name;
	private final String tenantId;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String phone;
	@Builder.Default private final Set<Attribute> attributes = Collections.emptySet();
}
