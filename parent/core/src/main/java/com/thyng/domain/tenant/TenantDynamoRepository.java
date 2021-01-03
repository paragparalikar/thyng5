package com.thyng.domain.tenant;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.map.IMap;
import com.thyng.persistence.CacheRepository;
import com.thyng.persistence.DynamoRepository;

import lombok.experimental.Delegate;

@Repository
public class TenantDynamoRepository implements TenantRepository {

	private final IMap<Integer, Tenant> cache;
	private final DynamoRepository<Tenant> dynamoRepository;
	@Delegate private final CacheRepository<Tenant> cacheRepository;
	
	public TenantDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		cache = hazelcastInstance.getMap(Tenant.TABLE_NAME);
		final String idProviderName = String.join("-", Tenant.TABLE_NAME, "id", "provider");
		final IAtomicLong idProvider = hazelcastInstance.getCPSubsystem().getAtomicLong(idProviderName);
		dynamoRepository = new DynamoRepository<>(mapper.newTableMapper(Tenant.class));
		cacheRepository = new CacheRepository<>(cache, idProvider, dynamoRepository);
	}
	
	@PostConstruct
	public void initialize() {
		dynamoRepository.initialize();
		cacheRepository.initialize();
	}

	@Override
	public boolean existsByName(Integer id, String name) {
		return 0 < cache.aggregate(Aggregators.count(), entry -> {
			final Tenant tenant = Tenant.class.cast(entry.getValue());
			return !tenant.getId().equals(id) && tenant.getName().trim().equalsIgnoreCase(name.trim());
		});
	}
	
}
