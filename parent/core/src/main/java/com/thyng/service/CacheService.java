package com.thyng.service;

import java.util.HashMap;
import java.util.Map;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Thing;
import com.thyng.repository.TenantAwareRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CacheService implements Lifecycle {
	
	private final Map<String, Thing> thingsCache = new HashMap<>();
	private final Map<String, Template> templatesCache = new HashMap<>();

	@NonNull private final TenantAwareRepository<Thing> thingRepository;
	@NonNull private final TenantAwareRepository<Template> templateRepository;
	
	@Override
	public void start() throws Exception {
		log.info("Attempting to eagerly cache all templates");
		templatesCache.putAll(templateRepository.findAll());
		log.info("Successfully eagerly cached all templates");
		log.info("Attempting to eagerly cache all things");
		thingsCache.putAll(thingRepository.findAll());
		log.info("Successfully eagerly cached all things");
	}
	
	public Thing thing(String id) {
		return thingsCache.get(id);
	}
	
	public Template template(String id) {
		return templatesCache.get(id);
	}
}
