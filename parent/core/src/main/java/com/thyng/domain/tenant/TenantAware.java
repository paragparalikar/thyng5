package com.thyng.domain.tenant;

public interface TenantAware {

	Long getTenantId();
	
	void setTenantId(Long tenantId);
	
}
