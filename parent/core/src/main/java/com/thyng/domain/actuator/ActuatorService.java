package com.thyng.domain.actuator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class ActuatorService {

	@Delegate
	private final ActuatorRepository actuatorRepository;
	
}
