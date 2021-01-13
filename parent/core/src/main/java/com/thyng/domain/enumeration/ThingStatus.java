package com.thyng.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ThingStatus {

	ONLINE("Online"), OFFLINE("Offline");
	
	private final String displayName;
	
}
