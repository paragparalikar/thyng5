package com.thyng.domain.intf;

import java.util.List;

public interface TenantAwareCrud<T extends TenantAwareModel<T>> extends TenantAwareSource<T>, Lifecycle {
	
	List<T> findAll();
	
	List<T> findAll(String tenantId);
	
	T save(T entity);
	
	T findById(String id);
	
	T findById(String tenantId, String id);
	
	T deleteById(String tenantId, String id);

	boolean existsByName(String tenantId, String id, String name);

}
