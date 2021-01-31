package com.thyng.service.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.TenantAwareSource;
import com.thyng.domain.model.Mapping;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.event.EventBus;
import com.thyng.repository.MappingRepository;
import com.thyng.service.evaluation.ScriptEvaluationService;
import com.thyng.util.Constant;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@RequiredArgsConstructor
public class ThingGroupMappingService implements Lifecycle {
	
	@NonNull private final EventBus eventService;
	@NonNull private final TenantAwareSource<Thing> thingSource;
	@NonNull private final TenantAwareSource<Template> templateSource;
	@NonNull private final TenantAwareSource<ThingGroup> thingGroupSource;
	@NonNull private final MappingRepository thingGroupMappingepository;
	@NonNull private final ScriptEvaluationService scriptEvaluationService;
	
	private final Consumer<Thing> thingMapper = this::mapThing;
	private final Consumer<Thing> thingUnmapper = this::deleteThing;
	private final Consumer<Template> templateMapper = this::mapTemplate;
	private final Consumer<Template> templateUnmapper = this::deleteTemplate;
	private final Consumer<ThingGroup> thingGroupMapper = this::mapThingGroup;
	private final Consumer<ThingGroup> thingGroupUnmapper = this::deleteThingGroup;
	
	@Override
	public void start() throws Exception {
		eventService.subscribe(Constant.createdTopic(Constant.THING), thingMapper);
		eventService.subscribe(Constant.updatedTopic(Constant.THING), thingMapper);
		eventService.subscribe(Constant.deletedTopic(Constant.THING), thingUnmapper);
		eventService.subscribe(Constant.createdTopic(Constant.TEMPALTE), templateMapper);
		eventService.subscribe(Constant.updatedTopic(Constant.TEMPALTE), templateMapper);
		eventService.subscribe(Constant.deletedTopic(Constant.TEMPALTE), templateUnmapper);
		eventService.subscribe(Constant.createdTopic(Constant.THING_GROUP), thingGroupMapper);
		eventService.subscribe(Constant.updatedTopic(Constant.THING_GROUP), thingGroupMapper);
		eventService.subscribe(Constant.deletedTopic(Constant.THING_GROUP), thingGroupUnmapper);
	}
	
	@Override
	public void stop() throws Exception {
		eventService.unsubscribe(Constant.createdTopic(Constant.THING), thingMapper);
		eventService.unsubscribe(Constant.updatedTopic(Constant.THING), thingMapper);
		eventService.unsubscribe(Constant.deletedTopic(Constant.THING), thingUnmapper);
		eventService.unsubscribe(Constant.createdTopic(Constant.TEMPALTE), templateMapper);
		eventService.unsubscribe(Constant.updatedTopic(Constant.TEMPALTE), templateMapper);
		eventService.unsubscribe(Constant.deletedTopic(Constant.TEMPALTE), templateUnmapper);
		eventService.unsubscribe(Constant.createdTopic(Constant.THING_GROUP), thingGroupMapper);
		eventService.unsubscribe(Constant.updatedTopic(Constant.THING_GROUP), thingGroupMapper);
		eventService.unsubscribe(Constant.deletedTopic(Constant.THING_GROUP), thingGroupUnmapper);
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
		final Collection<Thing> things = thingSource.findAll(thingGroup.getTenantId());
		final Set<String> thingIds = things.stream()
			.filter(thing -> evaluate(thingGroup, thing))
			.map(Thing::getId)
			.collect(Collectors.toSet());
		final Mapping mapping = thingGroupMappingepository.findById(thingGroup.getId());
		if(!mapping.getValues().equals(thingIds)) {
			mapping.getValues().clear();
			mapping.getValues().addAll(thingIds);
			thingGroupMappingepository.save(mapping);
		}
	}
	
	private void deleteThingGroup(ThingGroup thingGroup) {
		thingGroupMappingepository.delete(thingGroup.getId());
	}
	
	private void mapThing(Thing thing) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = thingGroupSource.findAll(thing.getTenantId());
		for(ThingGroup thingGroup : thingGroups) {
			final Mapping mapping = thingGroupMappingepository.findById(thingGroup.getId());
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
		thingGroupMappingepository.saveAll(mappings);
	}
	
	private void deleteThing(Thing thing) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = thingGroupSource.findAll(thing.getTenantId());
		for(ThingGroup thingGroup : thingGroups) {
			final Mapping mapping = thingGroupMappingepository.findById(thingGroup.getId());
			if(mapping.getValues().remove(thing.getId())) {
				mappings.add(mapping);
			}
		}
		thingGroupMappingepository.saveAll(mappings);
	}
	
	private void mapTemplate(Template template) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = thingGroupSource.findAll(template.getTenantId());
		final Collection<Thing> things = thingSource.findAll(template.getTenantId());
		things.stream()
			.filter(thing -> thing.getTemplateId().equals(template.getId()))
			.forEach(thing -> {
				for(ThingGroup thingGroup : thingGroups) {
					final Mapping mapping = thingGroupMappingepository.findById(thingGroup.getId());
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
		thingGroupMappingepository.saveAll(mappings);
	}
	
	private void deleteTemplate(Template template) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<ThingGroup> thingGroups = thingGroupSource.findAll(template.getTenantId());
		final Collection<Thing> things = thingSource.findAll(template.getTenantId());
		things.stream()
		.filter(thing -> thing.getTemplateId().equals(template.getId()))
		.forEach(thing -> {
			for(ThingGroup thingGroup : thingGroups) {
				final Mapping mapping = thingGroupMappingepository.findById(thingGroup.getId());
				if(mapping.getValues().remove(thing.getId())) {
					mappings.add(mapping);
				}
			}
		});
		thingGroupMappingepository.saveAll(mappings);
	}

}
