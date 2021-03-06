package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.enumeration.WindowBase;
import com.thyng.domain.enumeration.WindowType;
import com.thyng.domain.model.Window;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class WindowMapper implements Mapper<Window, Map<String, AttributeValue>> {

	@Override
	public Map<String, AttributeValue> unmap(Window window){
		return null == window ? null : new AttributeMap(new HashMap<>())
				.put("base", window.getBase())
				.put("type", window.getType())
				.put("span", window.getSpan())
				.put("timeout", window.getTimeout())
				.put("slidingSpan", window.getSlidingSpan());
	}
	
	@Override
	public Window map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return Window.builder()
				.base(map.getEnum("base", WindowBase.class))
				.type(map.getEnum("type", WindowType.class))
				.span(map.getLong("span"))
				.timeout(map.getLong("timeout"))
				.slidingSpan(map.getLong("slidingSpan"))
				.build();
	}
	
}
