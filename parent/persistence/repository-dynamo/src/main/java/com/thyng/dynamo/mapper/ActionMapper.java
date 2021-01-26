package com.thyng.dynamo.mapper;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

import com.thyng.domain.enumeration.ActionType;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Action.ActionBuilder;
import com.thyng.domain.model.MailAction;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@SuppressWarnings("rawtypes")
public class ActionMapper implements Mapper<Action, Map<String, AttributeValue>>{

	private final Map<ActionType, Function<Action, AttributeMap>> unmappers = new EnumMap<>(ActionType.class);
	private final Map<ActionType, Function<Map<String, AttributeValue>, ActionBuilder>> mappers = new EnumMap<>(ActionType.class);
	
	public ActionMapper() {
		mappers.put(ActionType.MAIL, this::mapMailAction);
		unmappers.put(ActionType.MAIL, this::unmapMailAction);
	}
	
	@Override
	public Action map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		final ActionType type = map.getEnum("type", ActionType.class);
		final Function<Map<String, AttributeValue>, ActionBuilder> function = mappers.getOrDefault(type, attrs -> null);
		final ActionBuilder actionBuilder = function.apply(attributes);
		if(null == actionBuilder) return null;
		return actionBuilder
			.id(map.getS("id"))
			.tenantId(map.getS("tenantId"))
			.name(map.getS("name"))
			.actionType(type)
			.enabled(map.getBool("enabled"))
			.rateLimit(map.getLong("rateLimit"))
			.build();
	}
	
	private ActionBuilder mapMailAction(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return MailAction.builder()
				.content(map.getS("content"))
				.subject(map.getS("subject"))
				.userGroupIds(Collections.unmodifiableSet(new HashSet<>(map.getSs("userGroupIds"))));
	}

	@Override
	public AttributeMap unmap(Action entity) {
		return null == entity ? null : unmappers.get(entity.getActionType())
				.apply(entity)
				.put("id", entity.getId())
				.put("name", entity.getName())
				.put("tenantId", entity.getTenantId())
				.put("enabled", entity.getEnabled())
				.put("rateLimit", entity.getRateLimit());
	}
	
	private AttributeMap unmapMailAction(Action entity) {
		final MailAction action = MailAction.class.cast(entity);
		return new AttributeMap(new HashMap<>())
				.put("type", ActionType.MAIL)
				.put("userGroupIds", action.getUserGroupIds())
				.put("subject", action.getSubject())
				.put("content", action.getContent());
	}

}
