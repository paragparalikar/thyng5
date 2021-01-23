package com.thyng.dynamo.mapper;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.thyng.domain.enumeration.ActionType;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.MailAction;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ActionMapper implements Mapper<Action, Map<String, AttributeValue>>{

	private final Map<ActionType, Function<Action, AttributeMap>> unmappers = new EnumMap<>(ActionType.class);
	private final Map<ActionType, Function<Map<String, AttributeValue>, Action>> mappers = new EnumMap<>(ActionType.class);
	
	public ActionMapper() {
		mappers.put(ActionType.MAIL, this::mapMailAction);
		unmappers.put(ActionType.MAIL, this::unmapMailAction);
	}
	
	@Override
	public Action map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		final ActionType type = map.getEnum("type", ActionType.class);
		final Function<Map<String, AttributeValue>, Action> function = mappers.getOrDefault(type, attrs -> null);
		final Action action = function.apply(attributes);
		if(null == action) return null;
		action.setId(map.getS("id"));
		action.setTenantId(map.getS("tenantId"));
		action.setName(map.getS("name"));
		action.setActionType(type);
		action.setEnabled(map.getBool("enabled"));
		action.setRateLimit(map.getLong("rateLimit"));
		return action;
	}
	
	private Action mapMailAction(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		final MailAction mailAction = MailAction.builder()
				.content(map.getS("content"))
				.subject(map.getS("subject"))
				.build();
		mailAction.getUserGroupIds().addAll(map.getSs("userGroupIds"));
		return mailAction;
	}

	@Override
	public AttributeMap unmap(Action entity) {
		return null == entity ? null : unmappers.get(entity.getActionType())
				.apply(entity)
				.put("id", entity.getId())
				.put("name", entity.getName())
				.put("tenantId", entity.getTenantId())
				.put("type", entity.getActionType())
				.put("enabled", entity.getEnabled())
				.put("rateLimit", entity.getRateLimit());
	}
	
	private AttributeMap unmapMailAction(Action entity) {
		final MailAction action = MailAction.class.cast(entity);
		return new AttributeMap(new HashMap<>())
				.put("userGroupIds", action.getUserGroupIds())
				.put("subject", action.getSubject())
				.put("content", action.getContent());
	}

}
