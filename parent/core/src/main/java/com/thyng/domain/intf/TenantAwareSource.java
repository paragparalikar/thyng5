package com.thyng.domain.intf;

import java.util.List;

public interface TenantAwareSource<T extends TenantAwareModel<T>> extends Source<T> {

	List<T> findAll(String tenantId);
	
}
