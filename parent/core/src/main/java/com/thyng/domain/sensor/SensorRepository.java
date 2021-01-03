package com.thyng.domain.sensor;

import java.util.List;

import com.thyng.persistence.Repository;

public interface SensorRepository extends Repository<Sensor, String> {

	List<Sensor> findByThingId(String thingId);
	
}
