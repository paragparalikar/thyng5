package com.thyng.repository;

import com.thyng.domain.intf.TenantAwareCrud;
import com.thyng.domain.intf.TenantAwareModel;

public interface TenantAwareRepository<T extends TenantAwareModel<T>> extends TenantAwareCrud<T> {

	
}
