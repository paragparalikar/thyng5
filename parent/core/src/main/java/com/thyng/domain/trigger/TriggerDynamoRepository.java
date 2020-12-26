package com.thyng.domain.trigger;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.thyng.persistence.AbstractRepository;

@Repository
public class TriggerDynamoRepository extends AbstractRepository<Trigger> implements TriggerRepository {

	public TriggerDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Trigger.TABLE_NAME, Trigger.class, mapper, hazelcastInstance);
	}

}
