package com.thyng.domain.sensor;

import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.util.StringUtil;
import com.thyng.domain.tenant.Tenant;
import com.thyng.persistence.AbstractRepository;

@Repository
public class SensorDynamoRepository extends AbstractRepository<Sensor> implements SensorRepository {

	public SensorDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Sensor.TABLE_NAME, Sensor.class, mapper, hazelcastInstance);
	}
	
	@Override
	protected boolean existsByName(Sensor entity, Sensor other, Tenant tenant) {
		final String tenantId = null == tenant ? null : tenant.getId();
		final String otherId = null == other ? null : other.getId();
		final String otherName = null == other ? null : other.getName();
		final String otherThingId = null == other ? null : other.getThingId(); 
		final String otherTenantId = null == other ? null : other.getTenantId();
		final String entityId = null == entity ? null : entity.getId();
		final String entityName = null == entity ? null : entity.getName();
		final String entityThingId = null == entity ? null : entity.getThingId();
		final String entityTenantId = null == entity ? null : entity.getTenantId();
		
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
