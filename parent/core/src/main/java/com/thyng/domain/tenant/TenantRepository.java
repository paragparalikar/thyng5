package com.thyng.domain.tenant;

import com.thyng.persistence.Repository;

public interface TenantRepository extends Repository<Tenant, Integer>{
	
	boolean existsByName(Integer id, String name);
	
}
