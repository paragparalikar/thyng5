package com.thyng.domain.intf;

import java.util.List;
import java.util.Map;

public interface TenantAwareCrud<T extends TenantAwareModel<T>, ID> {
	
	Map<ID, T> findAll();
	
	List<T> findAll(String tenantId);
	
	T save(T entity);
	
	T findById(String tenantId, String id);
	
	T deleteById(String tenantId, String id);

	boolean existsByName(String tenantId, String id, String name);

}
