package com.thyng.domain.thing;

import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.thyng.domain.Names;
import com.thyng.domain.tenant.TenantAwareRepository;

import lombok.NonNull;

@Repository
public class ThingDynamoRepository extends TenantAwareRepository<Thing> implements ThingRepository {
	
	private final FlakeIdGenerator sensorIdGenerator;
	private final FlakeIdGenerator actuatorIdGenerator;
	
	public ThingDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Thing.TABLE_NAME, Thing.class, mapper, hazelcastInstance);
		this.sensorIdGenerator = hazelcastInstance.getFlakeIdGenerator(Names.idGenerator("sensors"));
		this.actuatorIdGenerator = hazelcastInstance.getFlakeIdGenerator(Names.idGenerator("actuators"));
	}

	@Override
	public Thing save(@NonNull final Thing thing) {
		if(null != thing.getSensors()) {
			thing.getSensors().stream()
				.filter(sensor -> null == sensor.getId() || 0 == sensor.getId())
				.forEach(sensor -> sensor.setId(sensorIdGenerator.newId()));
		}
		if(null != thing.getActuators()) {
			thing.getActuators().stream()
				.filter(actuator -> null == actuator.getId() || 0 == actuator.getId())
				.forEach(actuator -> actuator.setId(actuatorIdGenerator.newId()));
		}
		return super.save(thing);
	}
	
	
	
}
