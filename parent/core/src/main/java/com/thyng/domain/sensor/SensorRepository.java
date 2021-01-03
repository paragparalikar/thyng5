package com.thyng.domain.sensor;

import java.util.List;

import com.thyng.persistence.Repository;

public interface SensorRepository extends Repository<Sensor, Integer> {

	List<Sensor> findByThingId(Integer thingId);
	
}
