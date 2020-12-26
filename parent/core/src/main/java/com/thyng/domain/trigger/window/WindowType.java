package com.thyng.domain.trigger.window;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WindowType {
	
	TUMBLING("Tumbling"), SLIDING("Sliding"), SESSION("Session");

	private final String displayName;
	
}
