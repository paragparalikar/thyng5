package com.thyng.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Dates {

	public String encodeToSeconds(long timestamp) {
		return Base62.encode((timestamp - Constant.EPOCH)/1000);
	}
	
	public long decodeBySeconds(String value) {
		return Base62.decode(value)*1000 + Constant.EPOCH;
	}
	
}
