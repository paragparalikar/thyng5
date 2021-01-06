package com.thyng.domain.tenant;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.core.HazelcastInstance;
import com.thyng.persistence.CacheRepository;
import com.thyng.persistence.DynamoRepository;

@Repository
public class TenantDynamoRepository extends CacheRepository<Tenant> implements TenantRepository {

	public TenantDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Tenant.TABLE_NAME, new DynamoRepository<>(mapper.newTableMapper(Tenant.class)), hazelcastInstance);		
	}

	@Override
	public boolean existsByName(Long id, String name) {
		return 0 < getCache().aggregate(Aggregators.count(), entry -> {
			final Tenant tenant = Tenant.class.cast(entry.getValue());
			return !tenant.getId().equals(id) && tenant.getName().trim().equalsIgnoreCase(name.trim());
		});
	}
	
}
