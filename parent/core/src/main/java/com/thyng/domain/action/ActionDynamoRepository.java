package com.thyng.domain.action;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.thyng.domain.tenant.TenantAwareRepository;

@Repository
public class ActionDynamoRepository extends TenantAwareRepository<Action> implements ActionRepository {

	public ActionDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Action.TABLE_NAME, Action.class, mapper, hazelcastInstance);
	}
}
