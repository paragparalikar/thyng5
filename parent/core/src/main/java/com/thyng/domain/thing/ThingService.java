package com.thyng.domain.thing;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class ThingService {

	@Delegate
	private final ThingRepository thingRepository;
	
}
