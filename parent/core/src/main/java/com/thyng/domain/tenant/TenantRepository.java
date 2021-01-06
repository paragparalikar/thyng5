package com.thyng.domain.tenant;

import com.thyng.persistence.Repository;

public interface TenantRepository extends Repository<Tenant, Long>{
	
	boolean existsByName(Long id, String name);
	
}
