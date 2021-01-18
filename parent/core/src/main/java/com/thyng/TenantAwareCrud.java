package com.thyng;

import java.util.List;
import java.util.Map;

import com.thyng.domain.intf.TenantAwareModel;

public interface TenantAwareCrud<T extends TenantAwareModel, ID> {
	
	List<T> findAll();
	
	List<T> findAll(String tenantId);
	
	T save(T entity);
	
	T findById(String tenantId, String id);
	
	T deleteById(String tenantId, String id);
	
	Map<ID, String> findAllNames(String tenantId);

	boolean existsByName(String tenantId, String id, String name);

}
