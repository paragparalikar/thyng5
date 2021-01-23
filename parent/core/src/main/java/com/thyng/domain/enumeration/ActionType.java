package com.thyng.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {

	SMS("SMS"), 
	MAIL("Mail"),
	POPUP("Popup"), 
	PUSH_MESSAGE("Push Message"), 
	WEBHOOK("Webhook"), 
	ACTUATE("Actuate");
	
	private final String displayName;
}
