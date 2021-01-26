package com.thyng.domain.intf;

public interface TenantAware<T extends TenantAware<T>> {

	String getTenantId();
	
	T withTenantId(String tenantId);
	
}
