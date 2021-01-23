package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.enumeration.Language;
import com.thyng.domain.model.UserGroup;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class UserGroupMapper implements Mapper<UserGroup, Map<String, AttributeValue>>{

	@Override
	public UserGroup map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		return UserGroup.builder()
				.id(map.getS("id"))
				.name(map.getS("name"))
				.tenantId(map.getS("tenantId"))
				.language(map.getEnum("language", Language.class))
				.script(map.getS("script"))
				.build();
	}

	@Override
	public Map<String, AttributeValue> unmap(UserGroup entity) {
		return null == entity ? null : new AttributeMap(new HashMap<>())
				.put("id", entity.getId())
				.put("name", entity.getName())
				.put("tenantId", entity.getTenantId())
				.put("script", entity.getScript())
				.put("language", entity.getLanguage());
	}

}
