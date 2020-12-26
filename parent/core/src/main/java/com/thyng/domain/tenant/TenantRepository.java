package com.thyng.domain.tenant;

import com.thyng.persistence.Repository;

public interface TenantRepository extends Repository<Tenant, String>{
	
	boolean existsByName(String id, String name);
	
}
