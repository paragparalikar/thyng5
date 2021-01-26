package com.thyng.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Mapping;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.User;
import com.thyng.domain.model.UserGroup;
import com.thyng.repository.MappingRepository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.util.Names;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
public class CacheService implements Lifecycle {
	
	private TenantAwareRepository<Thing> thingRepository;
	private TenantAwareRepository<ThingGroup> thingGroupRepository;
	private MappingRepository thingGroupMappingRepository;
	private TenantAwareRepository<Template> templateRepository;
	private TenantAwareRepository<User> userRepository;
	private TenantAwareRepository<UserGroup> userGroupRepository;
	private MappingRepository userGroupMappingRepository;
	private TenantAwareRepository<Trigger> triggerRepository;
	private TenantAwareRepository<Action> actionRepository;
	private final Map<String, Cache<?>> caches = new HashMap<>();
	private final Map<String, TenantAwareCache<?>> tenantAwareCaches = new HashMap<>();

	@Override
	public void start() throws Exception {
		caches.put(Names.THING, new Cache<>(thingRepository::findAll));
		tenantAwareCaches.put(Names.THING_GROUP, new TenantAwareCache<>(thingGroupRepository::findAll));
		caches.put(Names.THING_GROUP_MAPPING, new Cache<>(() -> findAll(thingGroupMappingRepository)));
		tenantAwareCaches.put(Names.TEMPALTE, new TenantAwareCache<>(templateRepository::findAll));
		tenantAwareCaches.put(Names.USER, new TenantAwareCache<>(userRepository::findAll));
		tenantAwareCaches.put(Names.USER_GROUP, new TenantAwareCache<>(userGroupRepository::findAll));
		caches.put(Names.USER_GROUP_MAPPING, new Cache<>(() -> findAll(userGroupMappingRepository)));
		tenantAwareCaches.put(Names.TRIGGER, new TenantAwareCache<>(triggerRepository::findAll));
		tenantAwareCaches.put(Names.ACTION, new TenantAwareCache<>(actionRepository::findAll));
		caches.putAll(tenantAwareCaches);
	}
	
	@Override
	public void stop() throws Exception {

	}
	
	private Map<String, Mapping> findAll(MappingRepository mappingRepository){
		return mappingRepository.findAll().stream().collect(Collectors.toMap(Mapping::getId, Function.identity()));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T findById(String id, String type) {
		return (T) caches.get(type).findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Collection<T> findAll(String type){
		return (Collection<T>) caches.get(type).findAll();
	}
	
	@SuppressWarnings("unchecked")
	public <T> Collection<T> findAllByTenantId(String tenantId, String type){
		return (Collection<T>) tenantAwareCaches.get(type).findByTenantId(tenantId);
	}
	
}

@RequiredArgsConstructor
class Cache<T extends Identifiable<T, String>> {
	
	private final Supplier<Map<String, T>> supplier;
	protected final Map<String, T> cache = new HashMap<>();
	
	protected void loadIfEmpty() {
		if(cache.isEmpty()) cache.putAll(supplier.get());
	}
	
	public T findById(String id) {
		loadIfEmpty();
		return cache.get(id);
	}
	
	public Collection<T> findAll(){
		loadIfEmpty();
		return cache.values();
	}
	
}

class TenantAwareCache<T extends TenantAwareModel<T>> extends Cache<T> {
	
	private final Map<String, Set<T>> tenantAwareCache = new HashMap<>();

	public TenantAwareCache(Supplier<Map<String, T>> supplier) {
		super(supplier);
	}
	
	@Override
	protected void loadIfEmpty() {
		super.loadIfEmpty();
		cache.values().forEach(item -> tenantAwareCache
				.computeIfAbsent(item.getTenantId(), id -> ConcurrentHashMap.newKeySet())
				.add(item));
	}
	
	public Collection<T> findByTenantId(String tenantId){
		return tenantAwareCache.get(tenantId);
	}
	
}
