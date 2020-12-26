package com.thyng.domain.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {

	ALERT("Alert"), WEBHOOK("Webhook"), ACTUATE("Actuate");
	
	private final String displayName;
}
