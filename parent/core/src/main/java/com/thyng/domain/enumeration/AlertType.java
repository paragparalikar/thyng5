package com.thyng.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertType {

	SMS("SMS"), MAIL("Mail"), PUSH_MESSAGE("Push Message"), POPUP("Popup");
	
	private final String displayName;
	
}
