package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.enumeration.ThingGroupType;
import com.thyng.domain.model.ThingGroup;
import com.thyng.util.Collectionz;
import com.thyng.util.Strings;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ThingGroupMapper implements DynamoMapper<ThingGroup> {

	@Override
	public ThingGroup map(Map<String, AttributeValue> attributes) {
		if(null == attributes || attributes.isEmpty()) return null;
		final ThingGroup thingGroup = new ThingGroup();
		thingGroup.setId(attributes.get("id").s());
		thingGroup.setName(attributes.get("name").s());
		if(attributes.containsKey("tenantId")) thingGroup.setTenantId(attributes.get("tenantId").s());
		thingGroup.setType(resolveType(attributes.get("type")));
		thingGroup.setScript(mapS(attributes.get("script")));
		thingGroup.setLanguage(mapLanguage(attributes.get("language")));
		thingGroup.getThingIds().addAll(mapSs(attributes.get("thingIds")));
		thingGroup.getTemplateIds().addAll(mapSs(attributes.get("templateIds")));
		return thingGroup;
	}
	
	private ThingGroupType resolveType(AttributeValue attribute) {
		return null == attribute || Strings.isBlank(attribute.s()) ? null : ThingGroupType.valueOf(attribute.s());
	}

	@Override
	public Map<String, AttributeValue> unmap(ThingGroup item) {
		if(null == item) return null;
		final Map<String, AttributeValue> map = new HashMap<>();
		map.put("id", AttributeValue.builder().s(item.getId()).build());
		map.put("name", AttributeValue.builder().s(item.getName()).build());
		map.put("tenantId", AttributeValue.builder().s(item.getTenantId()).build());
		map.put("type", AttributeValue.builder().s(item.getType().name()).build());
		if(null != item.getLanguage()) map.put("language", AttributeValue.builder().s(item.getLanguage().name()).build());
		if(Strings.isNotBlank(item.getScript())) map.put("script", AttributeValue.builder().s(item.getScript()).build());
		if(Collectionz.isNotNullOrEmpty(item.getThingIds())) map.put("thingIds", AttributeValue.builder().ss(item.getThingIds()).build());
		if(Collectionz.isNotNullOrEmpty(item.getTemplateIds())) map.put("templateIds", AttributeValue.builder().ss(item.getTemplateIds()).build());
		return map;
	}

}
