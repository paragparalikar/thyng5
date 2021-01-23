package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.model.User;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RequiredArgsConstructor
public class UserMapper implements Mapper<User, Map<String, AttributeValue>>{

	@NonNull private final AttributeMapper attributesMapper;
	
	@Override
	public User map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final AttributeMap map = new AttributeMap(attributes);
		final User user = User.builder()
				.id(map.getS("id"))
				.name(map.getS("name"))
				.tenantId(map.getS("tenantId"))
				.firstName(map.getS("firstName"))
				.lastName(map.getS("lastName"))
				.email(map.getS("email"))
				.phone(map.getS("phone"))
				.build();
		user.getAttributes().addAll(attributesMapper.map(map.getSs("attributes")));
		return user;
	}

	@Override
	public Map<String, AttributeValue> unmap(User entity) {
		return null == entity ? null : new AttributeMap(new HashMap<>())
				.put("id", entity.getId())
				.put("name", entity.getName())
				.put("tenantId", entity.getTenantId())
				.put("firstName", entity.getFirstName())
				.put("lastName", entity.getLastName())
				.put("email", entity.getEmail())
				.put("phone", entity.getPhone())
				.put("attributes", attributesMapper.unmap(entity.getAttributes()));
	}

}
