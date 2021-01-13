package com.thyng.repository;

import java.util.List;
import java.util.Optional;

import com.thyng.domain.intf.Callback;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.TenantAwareModel;

public interface MultiTenantRepository<T extends TenantAwareModel> extends Lifecycle {

	void count(String tenantId, Callback<Long> callback);
	
	void findAll(Callback<List<T>> callback);
	
	void findAll(String tenantId, Callback<List<T>> callback);
	
	void save(T entity, Callback<T> callback);
	
	void findById(String tenantId, String id, Callback<Optional<T>> callback);
	
	void deleteById(String tenantId, String id, Callback<T> callback);

	void existsByName(String tenantId, String id, String name, Callback<Boolean> callback);
	
}
