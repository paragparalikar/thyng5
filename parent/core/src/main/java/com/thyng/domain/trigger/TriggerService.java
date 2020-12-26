package com.thyng.domain.trigger;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class TriggerService {

	@Delegate
	private final TriggerRepository triggerRepository;
	
}
