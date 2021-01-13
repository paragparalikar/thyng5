package com.thyng.repository.hazelcast;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HazelcastNames {
	
	private final String DELIMITER = "-";

	public String idGenerator(@NonNull final String cacheName) {
		return String.join(DELIMITER, cacheName, "id", "generator");
	}
	
	public String lock(@NonNull final String cacheName) {
		return String.join(DELIMITER, cacheName, "lock");
	}	
	
}
