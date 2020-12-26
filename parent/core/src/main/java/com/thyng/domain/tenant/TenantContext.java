package com.thyng.domain.tenant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TenantContext {

	private final ThreadLocal<Tenant> threadLocal = new ThreadLocal<>();
	
	public Tenant getTenant() {
		return threadLocal.get();
	}
	
	public void setTenant(final Tenant tenant) {
		threadLocal.set(tenant);
	}
}
