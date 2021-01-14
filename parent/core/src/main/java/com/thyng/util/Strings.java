package com.thyng.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Strings {

	public boolean isBlank(String value) {
		return null == value || 0 == value.trim().length();
	}
	
	public boolean isNotBlank(String value) {
		return !isBlank(value);
	}
	
	public String nullIfBlank(String value) {
		return isBlank(value) ? null : value;
	}
	
	public String emptyIfNull(String value) {
		return null == value ? "" : value;
	}
}
