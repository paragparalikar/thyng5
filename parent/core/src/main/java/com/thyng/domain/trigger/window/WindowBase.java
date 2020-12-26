package com.thyng.domain.trigger.window;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WindowBase {

	COUNT("Count"), TIME("Time");
	
	private final String displayName;
	
}
