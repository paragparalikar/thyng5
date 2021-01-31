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
public class ThingInfo implements TenantAwareModel<ThingInfo> {
	private static final long serialVersionUID = -4841182871379677017L;

	private final String id;
	private final String name;
	private final String tenantId;
	
	private final boolean online;
	private final long lastStatusChangeTimestamp;

}
