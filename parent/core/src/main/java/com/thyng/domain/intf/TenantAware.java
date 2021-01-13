package com.thyng.domain.intf;

public interface TenantAware {

	String getTenantId();
	
	void setTenantId(String tenantId);
	
}
