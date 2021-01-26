package com.thyng.domain.model;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class Gateway implements TenantAwareModel<Gateway> {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final String name;
	private final String tenantId;
	private final String publicKey;
	private final String privateKey;

}
