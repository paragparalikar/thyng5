package com.thyng.domain.intf;

public interface Identifiable<T extends Identifiable<T>> {

	String getId();
	
	T withId(String id);
	
}
