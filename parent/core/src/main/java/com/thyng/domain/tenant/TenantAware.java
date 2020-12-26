package com.thyng.domain.tenant;

public interface TenantAware {

	String getTenantId();
	
	void setTenantId(String tenantId);
	
}
