package com.thyng.domain.thing;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.thyng.domain.Identifiable;
import com.thyng.domain.Names;
import com.thyng.domain.tenant.TenantAwareRepository;

import lombok.NonNull;

@Repository
public class ThingDynamoRepository extends TenantAwareRepository<Thing> implements ThingRepository {
	
	private final IAtomicLong sensorIdProvider;
	private final IAtomicLong actuatorIdProvider;
	
	public ThingDynamoRepository(DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(Thing.TABLE_NAME, Thing.class, mapper, hazelcastInstance);
		this.sensorIdProvider = hazelcastInstance.getCPSubsystem().getAtomicLong(Names.idGenerator("sensors"));
		this.actuatorIdProvider = hazelcastInstance.getCPSubsystem().getAtomicLong(Names.idGenerator("actuators"));
	}

	@Override
	protected void load(Consumer<Thing> consumer) {
		final AtomicLong sensorMaxId = new AtomicLong();
		final AtomicLong actuatorMaxId = new AtomicLong();
		super.load(consumer.andThen(thing -> {
			if(null != thing.getSensors()) {
				final long maxSensorId = thing.getSensors().stream()
					.map(Identifiable::getId)
					.map(id -> Long.parseUnsignedLong(id, Character.MAX_RADIX))
					.max(Comparator.naturalOrder())
					.orElse(0L);
				sensorMaxId.updateAndGet(value -> Math.max(value, maxSensorId));
			}
		}).andThen(thing -> {
			if(null != thing.getActuators()) {
				final long maxSensorId = thing.getActuators().stream()
					.map(Identifiable::getId)
					.map(id -> Long.parseUnsignedLong(id, Character.MAX_RADIX))
					.max(Comparator.naturalOrder())
					.orElse(0L);
				actuatorMaxId.updateAndGet(value -> Math.max(value, maxSensorId));
			}
		}));
		final long sensorMaxIdValue = sensorMaxId.get();
		sensorIdProvider.alter(value -> Math.max(value, sensorMaxIdValue));
		final long actuatorMaxIdValue = actuatorMaxId.get();
		actuatorIdProvider.alter(value -> Math.max(value, actuatorMaxIdValue));
	}
	
	
	@Override
	public Thing save(@NonNull final Thing thing) {
		if(null != thing.getSensors()) {
			thing.getSensors().stream()
				.filter(sensor -> !StringUtils.hasText(sensor.getId()) || "0".equals(sensor.getId().trim()))
				.forEach(sensor -> {
					sensor.setId(Long.toString(sensorIdProvider.incrementAndGet(), Character.MAX_RADIX));
				});
		}
		if(null != thing.getActuators()) {
			thing.getActuators().stream()
				.filter(actuator -> !StringUtils.hasText(actuator.getId()) || "0".equals(actuator.getId().trim()))
				.forEach(actuator -> {
					actuator.setId(Long.toString(actuatorIdProvider.incrementAndGet(), Character.MAX_RADIX));
				});
		}
		return super.save(thing);
	}
	
	
	
}
