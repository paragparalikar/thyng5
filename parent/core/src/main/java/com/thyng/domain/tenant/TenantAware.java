package com.thyng.domain.tenant;

public interface TenantAware {

	Integer getTenantId();
	
	void setTenantId(Integer tenantId);
	
}
