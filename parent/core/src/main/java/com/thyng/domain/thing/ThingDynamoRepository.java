package com.thyng.domain.thing;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
				.filter(sensor -> !StringUtils.hasText(sensor.getId()) || "0".equals(sensor.getId().trim()))
				.forEach(sensor -> {
					sensor.setId(Long.toString(sensorIdGenerator.newId(), Character.MAX_RADIX));
				});
		}
		if(null != thing.getActuators()) {
			thing.getActuators().stream()
				.filter(actuator -> !StringUtils.hasText(actuator.getId()) || "0".equals(actuator.getId().trim()))
				.forEach(actuator -> {
					actuator.setId(Long.toString(actuatorIdGenerator.newId(), Character.MAX_RADIX));
				});
		}
		return super.save(thing);
	}
	
	
	
}
