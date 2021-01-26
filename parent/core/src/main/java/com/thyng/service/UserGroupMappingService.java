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
import com.thyng.domain.model.User;
import com.thyng.domain.model.UserGroup;
import com.thyng.repository.MappingRepository;
import com.thyng.util.Names;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@RequiredArgsConstructor
public class UserGroupMappingService implements Lifecycle {

	@NonNull private final CacheService cacheService;
	@NonNull private final EventService eventService;
	@NonNull private final MappingRepository repository;
	@NonNull private final ScriptEvaluationService scriptEvaluationService;
	
	@Override
	public void start() throws Exception {
		eventService.subscribe(Names.USER_CREATED, this::mapUser);
		eventService.subscribe(Names.USER_UPDATED, this::mapUser);
		eventService.subscribe(Names.USER_DELETED, this::deleteUser);
		eventService.subscribe(Names.USER_GROUP_CREATED, this::mapUserGroup);
		eventService.subscribe(Names.USER_GROUP_UPDATED, this::mapUserGroup);
		eventService.subscribe(Names.USER_GROUP_DELETED, this::deleteUserGroup);
	}
	
	@Override
	public void stop() throws Exception {
		eventService.unsubscribe(Names.USER_CREATED, this::mapUser);
		eventService.unsubscribe(Names.USER_UPDATED, this::mapUser);
		eventService.unsubscribe(Names.USER_DELETED, this::deleteUser);
		eventService.unsubscribe(Names.USER_GROUP_CREATED, this::mapUserGroup);
		eventService.unsubscribe(Names.USER_GROUP_UPDATED, this::mapUserGroup);
		eventService.unsubscribe(Names.USER_GROUP_DELETED, this::deleteUserGroup);
	}
	
	private boolean evaluate(UserGroup userGroup, User user) {
		try {
			final Map<String, Object> params = new HashMap<>();
			params.put("user", user);
			params.put("userGroup", userGroup);
			return (boolean) scriptEvaluationService.evaluate(userGroup.getScript(), 
					userGroup.getLanguage(), params);
		} catch(ScriptException e) {
			log.error("Script evaluation failed for user group " + userGroup.getName(), e);
		}
		return false;
	}
	
	private void mapUserGroup(UserGroup userGroup) {
		final Collection<User> users = cacheService.findAllByTenantId(userGroup.getTenantId(), Names.USER);
		final Set<String> userIds = users.stream()
			.filter(user -> evaluate(userGroup, user))
			.map(User::getId)
			.collect(Collectors.toSet());
		final Mapping mapping = cacheService.findById(userGroup.getId(), Names.USER_GROUP_MAPPING);
		if(!mapping.getValues().equals(userIds)) {
			mapping.getValues().clear();
			mapping.getValues().addAll(userIds);
			repository.save(mapping, Callback.<Mapping>builder()
					.failure(throwable -> log.error("Failed to save user group mappings for user group " + userGroup.getName(), throwable))
					.build());
		}
	}
	
	private void deleteUserGroup(UserGroup userGroup) {
		repository.delete(userGroup.getId(), Callback.<String>builder()
				.failure(throwable -> log.error("Failed to delete user group mappings for user group " + userGroup.getName(), throwable))
				.build());
	}
	
	private void mapUser(User user) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<UserGroup> userGroups = cacheService.findAllByTenantId(user.getTenantId(), Names.USER_GROUP);
		for(UserGroup userGroup : userGroups) {
			final Mapping mapping = cacheService.findById(userGroup.getId(), Names.USER_GROUP_MAPPING);
			if(evaluate(userGroup, user)) {
				if(mapping.getValues().add(user.getId())) {
					mappings.add(mapping);
				}
			} else {
				if(mapping.getValues().remove(user.getId())) {
					mappings.add(mapping);
				}
			}
		}
		repository.saveAll(mappings, Callback.<Collection<Mapping>>builder()
				.failure(throwable -> log.error("Failed to save user group mappings for user" + user.getName(), throwable))
				.build());
	}
	
	private void deleteUser(User user) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<UserGroup> userGroups = cacheService.findAllByTenantId(user.getTenantId(), Names.USER_GROUP);
		for(UserGroup userGroup : userGroups) {
			final Mapping mapping = cacheService.findById(userGroup.getId(), Names.USER_GROUP_MAPPING);
			if(mapping.getValues().remove(user.getId())) {
				mappings.add(mapping);
			}
		}
		repository.saveAll(mappings, Callback.<Collection<Mapping>>builder()
				.failure(throwable -> log.error("Failed to save user group mappings for user" + user.getName(), throwable))
				.build());
	}
	
}
