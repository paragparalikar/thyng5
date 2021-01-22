package com.thyng.util;

import java.util.Collection;
import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Collectionz {

	public boolean isNullOrEmpty(Collection<?> items) {
		return null == items || items.isEmpty();
	}
	
	public boolean isNullOrEmpty(Map<?, ?> map) {
		return null == map || map.isEmpty();
	}
	
	public boolean isNotNullOrEmpty(Collection<?> items) {
		return !isNullOrEmpty(items);
	}
	
	public boolean isNotNullOrEmpty(Map<?, ?> map) {
		return !isNullOrEmpty(map);
	}
	
}
