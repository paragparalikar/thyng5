package com.thyng.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalEventService implements EventService {
	
	private final Map<String, List<Consumer<Object>>> subscribers = new HashMap<>();

	@Override
	public void publish(String topic, Object event) {
		subscribers.computeIfAbsent(topic, t -> Collections.emptyList()).forEach(subscriber -> {
			try { 
				subscriber.accept(event);
			} catch(Exception e) {
				log.error("Event subscriber failed", e);
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void subscribe(String topic, Consumer<T> callback) {
		subscribers.computeIfAbsent(topic, t -> new LinkedList<>()).add((Consumer<Object>) callback);
	}

	@Override
	public <T> void unsubscribe(String topic, Consumer<T> callback) {
		subscribers.computeIfAbsent(topic, t -> Collections.emptyList()).remove(callback);
	}

}
