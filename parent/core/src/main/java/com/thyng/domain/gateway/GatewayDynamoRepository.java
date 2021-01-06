package com.thyng.domain.gateway;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.thyng.domain.tenant.TenantAwareRepository;

@Repository
public class GatewayDynamoRepository extends TenantAwareRepository<Gateway> implements GatewayRepository {

	public GatewayDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Gateway.TABLE_NAME, Gateway.class, mapper, hazelcastInstance);
	}

}
