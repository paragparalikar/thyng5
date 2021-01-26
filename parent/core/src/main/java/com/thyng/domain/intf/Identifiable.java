package com.thyng.domain.intf;

public interface Identifiable<T extends Identifiable<T, ID>, ID> {

	ID getId();
	
	T withId(ID id);
	
}
