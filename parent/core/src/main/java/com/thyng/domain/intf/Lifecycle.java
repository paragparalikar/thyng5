package com.thyng.domain.intf;

public interface Lifecycle {
	
	default void start() throws Exception {}
	
	default void stop() throws Exception {}

}
