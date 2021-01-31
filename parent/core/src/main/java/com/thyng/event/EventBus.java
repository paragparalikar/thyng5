package com.thyng.event;

import java.util.function.Consumer;

import com.thyng.domain.intf.Lifecycle;

public interface EventBus extends Lifecycle {

	void publish(String topic, Object event);
	
	<T> void subscribe(String topic, Consumer<T> callback);
	
	<T> void unsubscribe(String topic, Consumer<T> callback);
	
}
