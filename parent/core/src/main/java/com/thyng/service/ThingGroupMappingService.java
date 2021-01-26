package com.thyng.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Mapping;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.repository.MappingRepository;
import com.thyng.util.Names;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@RequiredArgsConstructor
public class ThingGroupMappingService implements Lifecycle {
	
	@NonNull private final CacheService cacheService;
	@NonNull private final EventService eventService;
	@NonNull private final MappingRepository repository;
	@NonNull private final ScriptEvaluationService scriptEvaluationService;
	
	@Override
	public void start() throws Exception {
		eventService.subscribe(Names.THING_CREATED, this::mapThing);
		eventService.subscribe(Names.THING_UPDATED, this::mapThing);
		eventService.subscribe(Names.THING_DELETED, this::deleteThing);
		eventService.subscribe(Names.TEMPLATE_CREATED, this::mapTemplate);
		eventService.subscribe(Names.TEMPLATE_UPDATED, this::mapTemplate);
		eventService.subscribe(Names.TEMPLATE_DELETED, this::deleteTemplate);
		eventService.subscribe(Names.THING_GROUP_CREATED, this::mapThingGroup);
		eventService.subscribe(Names.THING_GROUP_UPDATED, this::mapThingGroup);
		eventService.subscribe(Names.THING_GROUP_DELETED, this::deleteThingGroup);
	}
	
	@Override
	public void stop() throws Exception {
		eventService.unsubscribe(Names.THING_CREATED, this::mapThing);
		eventService.unsubscribe(Names.THING_UPDATED, this::mapThing);
		eventService.unsubscribe(Names.THING_DELETED, this::deleteThing);
		eventService.unsubscribe(Names.TEMPLATE_CREATED, this::mapTemplate);
		eventService.unsubscribe(Names.TEMPLATE_UPDATED, this::mapTemplate);
		eventService.unsubscribe(Names.TEMPLATE_DELETED, this::deleteTemplate);
		eventService.unsubscribe(Names.THING_GROUP_CREATED, this::mapThingGroup);
		eventService.unsubscribe(Names.THING_GROUP_UPDATED, this::mapThingGroup);
		eventService.unsubscribe(Names.THING_GROUP_DELETED, this::deleteThingGroup);
	}
	
	private boolean evaluate(ThingGroup thingGroup, Thing thing) {
		try {
			final Map<String, Object> params = new HashMap<>();
			params.put("thing", thing);
			params.put("thingGroup", thingGroup);
			return (boolean) scriptEvaluationService.evaluate(thingGroup.getScript(), 
					thingGroup.getLanguage(), params);
		} catch(ScriptException e) {
			log.error("Script evaluation failed for thing group " + thingGroup.getName(), e);
		}
		return false;
	}
	
	private void mapThingGroup(ThingGroup thingGroup) {
		final Collection<Thing> things = cacheService.findAllByTenantId(thingGroup.getTenantId(), Names.THING);
		final Set<String> thingIds = things.stream()
			.filter(thing -> evaluate(thingGroup, thing))
			.map(Thing::getId)
			.collect(Collectors.toSet());
		final Mapping mapping = cacheService.findById(thingGroup.getId(), Names.THING_GROUP_MAPPING);
		if(!mapping.getValues().equals(thingIds)) {
			mapping.getValues().clear();
			mapping.getValues().addAll(thingIds);
			repository.save(mapping, Callback.<Mapping>builder()
					.failure(throwable -> log.error("Failed to save thing group mappings for thing group " + thingGroup.getName(), throwable))
					.build());
		}
	}
	
	private void deleteThingGroup(ThingGroup thingGroup) {
		repository.delete(thingGroup.getId(), Callback.<String>builder()
				.failure(throwable -> log.error("Failed to delete thing group mappings for thing group " + thingGroup.getName(), throwable))
				.build());
	}
	
	private void mapThing(Thing thing) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = cacheService.findAllByTenantId(thing.getTenantId(), Names.THING_GROUP);
		for(ThingGroup thingGroup : thingGroups) {
			final Mapping mapping = cacheService.findById(thingGroup.getId(), Names.THING_GROUP_MAPPING);
			if(evaluate(thingGroup, thing)) {
				if(mapping.getValues().add(thing.getId())) {
					mappings.add(mapping);
				}
			} else {
				if(mapping.getValues().remove(thing.getId())) {
					mappings.add(mapping);
				}
			}
		}
		repository.saveAll(mappings, Callback.<Collection<Mapping>>builder()
				.failure(throwable -> log.error("Failed to save thing group mappings for thing " + thing.getName(), throwable))
				.build());
	}
	
	private void deleteThing(Thing thing) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = cacheService.findAllByTenantId(thing.getTenantId(), Names.THING_GROUP);
		for(ThingGroup thingGroup : thingGroups) {
			final Mapping mapping = cacheService.findById(thingGroup.getId(), Names.THING_GROUP_MAPPING);
			if(mapping.getValues().remove(thing.getId())) {
				mappings.add(mapping);
			}
		}
		repository.saveAll(mappings, Callback.<Collection<Mapping>>builder()
				.failure(throwable -> log.error("Failed to save thing group mappings for thing " + thing.getName(), throwable))
				.build());
	}
	
	private void mapTemplate(Template template) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = cacheService.findAllByTenantId(template.getTenantId(), Names.THING_GROUP);
		final Collection<Thing> things = cacheService.findAllByTenantId(template.getTenantId(), Names.THING);
		things.stream()
			.filter(thing -> thing.getTemplateId().equals(template.getId()))
			.forEach(thing -> {
				for(ThingGroup thingGroup : thingGroups) {
					final Mapping mapping = cacheService.findById(thingGroup.getId(), Names.THING_GROUP_MAPPING);
					if(evaluate(thingGroup, thing)) {
						if(mapping.getValues().add(thing.getId())) {
							mappings.add(mapping);
						}
					} else {
						if(mapping.getValues().remove(thing.getId())) {
							mappings.add(mapping);
						}
					}
				}
			});
		repository.saveAll(mappings, Callback.<Collection<Mapping>>builder()
				.failure(throwable -> log.error("Failed to save thing group mappings for template " + template.getName(), throwable))
				.build());
	}
	
	private void deleteTemplate(Template template) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = cacheService.findAllByTenantId(template.getTenantId(), Names.THING_GROUP);
		final Collection<Thing> things = cacheService.findAllByTenantId(template.getTenantId(), Names.THING);
		things.stream()
		.filter(thing -> thing.getTemplateId().equals(template.getId()))
		.forEach(thing -> {
			for(ThingGroup thingGroup : thingGroups) {
				final Mapping mapping = cacheService.findById(thingGroup.getId(), Names.THING_GROUP_MAPPING);
				if(mapping.getValues().remove(thing.getId())) {
					mappings.add(mapping);
				}
			}
		});
		repository.saveAll(mappings, Callback.<Collection<Mapping>>builder()
				.failure(throwable -> log.error("Failed to save thing group mappings for template " + template.getName(), throwable))
				.build());
		}

}
