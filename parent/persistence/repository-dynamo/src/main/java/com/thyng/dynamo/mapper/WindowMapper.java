package com.thyng.dynamo.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.enumeration.WindowBase;
import com.thyng.domain.enumeration.WindowType;
import com.thyng.domain.model.Window;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class WindowMapper implements Mapper<Window> {

	@Override
	public Map<String, AttributeValue> unmap(Window window){
		if(null == window) return Collections.emptyMap();
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("base", AttributeValue.builder().s(window.getBase().toString()).build());
		map.put("type", AttributeValue.builder().s(window.getType().toString()).build());
		map.put("tumblingInterval", AttributeValue.builder().n(window.getTumblingInterval().toString()).build());
		map.put("slidingInterval", AttributeValue.builder().n(window.getSlidingInterval().toString()).build());
		return map;
	}
	
	@Override
	public Window map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final Window item = new Window();
		item.setBase(WindowBase.valueOf(attributes.get("base").s()));
		item.setType(WindowType.valueOf(attributes.get("type").s()));
		item.setSlidingInterval(Long.parseLong(attributes.get("slidingInterval").n()));
		item.setTumblingInterval(Long.parseLong(attributes.get("tumblingInterval").n()));
		return item;
	}
	
}
