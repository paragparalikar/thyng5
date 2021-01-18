package com.thyng.util;

import java.util.Collection;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Collectionz {

	public boolean isNullOrEmpty(Collection<?> items) {
		return null == items || items.isEmpty();
	}
	
	public boolean isNotNullOrEmpty(Collection<?> items) {
		return !isNullOrEmpty(items);
	}
	
}
