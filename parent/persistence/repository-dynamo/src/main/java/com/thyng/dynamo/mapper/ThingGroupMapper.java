package com.thyng.dynamo.mapper;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.enumeration.Language;
import com.thyng.domain.enumeration.ThingGroupType;
import com.thyng.domain.model.ThingGroup;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class ThingGroupMapper implements Mapper<ThingGroup, Map<String, AttributeValue>> {

	@Override
	public ThingGroup map(Map<String, AttributeValue> attributes) {
		final AttributeMap map = new AttributeMap(attributes);
		final ThingGroup thingGroup = ThingGroup.builder()
			.id(map.getS("id"))
			.name(map.getS("name"))
			.tenantId(map.getS("tenantId"))
			.type(map.getEnum("type", ThingGroupType.class))
			.script(map.getS("script"))
			.language(map.getEnum("language", Language.class))
			.build();
		thingGroup.getThingIds().addAll(map.getSs("thingIds"));
		thingGroup.getTemplateIds().addAll(map.getSs("templateIds"));
		return thingGroup;
	}

	@Override
	public Map<String, AttributeValue> unmap(ThingGroup item) {
		return null == item ? null : new AttributeMap(new HashMap<>())
				.put("id", item.getId())
				.put("name", item.getName())
				.put("tenantId", item.getTenantId())
				.put("type", item.getType())
				.put("script", item.getScript())
				.put("language", item.getLanguage())
				.put("thingIds", item.getThingIds())
				.put("templateIds", item.getTemplateIds());
	}

}
