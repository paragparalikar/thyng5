package com.thyng.domain.actuator;

import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.util.StringUtil;
import com.thyng.domain.tenant.Tenant;
import com.thyng.persistence.AbstractRepository;

@Repository
public class ActuatorDynamoRepository extends AbstractRepository<Actuator> implements ActuatorRepository {
	
	public ActuatorDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Actuator.TABLE_NAME, Actuator.class, mapper, hazelcastInstance);
	}

	@Override
	protected boolean existsByName(Actuator entity, Actuator other, Tenant tenant) {
		final Integer tenantId = null == tenant ? null : tenant.getId();
		final Integer otherId = null == other ? null : other.getId();
		final String otherName = null == other ? null : other.getName();
		final Integer otherThingId = null == other ? null : other.getThingId(); 
		final Integer otherTenantId = null == other ? null : other.getTenantId();
		final Integer entityId = null == entity ? null : entity.getId();
		final String entityName = null == entity ? null : entity.getName();
		final Integer entityThingId = null == entity ? null : entity.getThingId();
		final Integer entityTenantId = null == entity ? null : entity.getTenantId();
		
		if(null == tenantId) return
				Objects.equals(entityThingId, otherThingId)
				&& !Objects.equals(entityId, otherId) 
				&& StringUtil.equalsIgnoreCase(entityName, otherName);
		
		else return Objects.equals(tenantId, entityTenantId) 
				&& Objects.equals(tenantId, otherTenantId)
				&& Objects.equals(entityThingId, otherThingId)
				&& !Objects.equals(entityId, otherId) 
				&& StringUtil.equalsIgnoreCase(entityName, otherName);
	}
	
}
