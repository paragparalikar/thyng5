package com.thyng.repository;

import java.util.List;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.TenantAwareModel;

public interface TenantAwareRepository<T extends TenantAwareModel> extends Lifecycle {

	List<T> findAll();
	
	List<T> findAll(String tenantId);
	
	T save(T entity);
	
	T findById(String tenantId, String id);
	
	T deleteById(String tenantId, String id);

	boolean existsByName(String tenantId, String id, String name);
}
