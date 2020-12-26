package com.thyng.domain.sensor;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class SensorService {

	@Delegate
	private final SensorRepository sensorRepository;
	
}
