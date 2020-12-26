package com.thyng.domain.gateway;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.thyng.persistence.AbstractRepository;

@Repository
public class GatewayDynamoRepository extends AbstractRepository<Gateway> implements GatewayRepository {

	public GatewayDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Gateway.TABLE_NAME, Gateway.class, mapper, hazelcastInstance);
	}

}
