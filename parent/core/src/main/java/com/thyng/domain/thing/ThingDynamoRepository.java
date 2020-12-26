package com.thyng.domain.thing;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.thyng.persistence.AbstractRepository;

@Repository
public class ThingDynamoRepository extends AbstractRepository<Thing> implements ThingRepository {
	
	public ThingDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Thing.TABLE_NAME, Thing.class, mapper, hazelcastInstance);
	}

}
