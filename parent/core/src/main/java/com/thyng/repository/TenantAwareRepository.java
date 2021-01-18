package com.thyng.repository;

import com.thyng.TenantAwareCrud;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.TenantAwareModel;

public interface TenantAwareRepository<T extends TenantAwareModel> extends TenantAwareCrud<T, String>, Lifecycle {

	
}
