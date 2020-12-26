package com.thyng.domain.trigger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {

	TELEMETRY("Telemetry"), 
	STATUS_CHANGE("Status Change");
	
	private final String displayName;
	
}
