package com.thyng.service;

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
import com.thyng.domain.model.User;
import com.thyng.domain.model.UserGroup;
import com.thyng.repository.MappingRepository;
import com.thyng.util.Constant;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@RequiredArgsConstructor
public class UserGroupMappingService implements Lifecycle {

	@NonNull private final EventService eventService;
	@NonNull private final TenantAwareSource<User> userSource;
	@NonNull private final TenantAwareSource<UserGroup> userGroupSource;
	@NonNull private final MappingRepository userGroupMappingRepository;
	@NonNull private final ScriptEvaluationService scriptEvaluationService;
	
	private final Consumer<User> userMapper = this::mapUser;
	private final Consumer<User> userUnmapper = this::deleteUser;
	private final Consumer<UserGroup> userGroupMapper = this::mapUserGroup;
	private final Consumer<UserGroup> userGroupUnmapper = this::deleteUserGroup;
	
	@Override
	public void start() throws Exception {
		eventService.subscribe(Constant.createdTopic(Constant.USER), userMapper);
		eventService.subscribe(Constant.updatedTopic(Constant.USER), userMapper);
		eventService.subscribe(Constant.deletedTopic(Constant.USER), userUnmapper);
		eventService.subscribe(Constant.createdTopic(Constant.USER_GROUP), userGroupMapper);
		eventService.subscribe(Constant.updatedTopic(Constant.USER_GROUP), userGroupMapper);
		eventService.subscribe(Constant.deletedTopic(Constant.USER_GROUP), userGroupUnmapper);
	}
	
	@Override
	public void stop() throws Exception {
		eventService.unsubscribe(Constant.createdTopic(Constant.USER), userMapper);
		eventService.unsubscribe(Constant.updatedTopic(Constant.USER), userMapper);
		eventService.unsubscribe(Constant.deletedTopic(Constant.USER), userUnmapper);
		eventService.unsubscribe(Constant.createdTopic(Constant.USER_GROUP), userGroupMapper);
		eventService.unsubscribe(Constant.updatedTopic(Constant.USER_GROUP), userGroupMapper);
		eventService.unsubscribe(Constant.deletedTopic(Constant.USER_GROUP), userGroupUnmapper);
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
		final Collection<User> users = userSource.findAll(userGroup.getTenantId());
		final Set<String> userIds = users.stream()
			.filter(user -> evaluate(userGroup, user))
			.map(User::getId)
			.collect(Collectors.toSet());
		final Mapping mapping = userGroupMappingRepository.findById(userGroup.getId());
		if(!mapping.getValues().equals(userIds)) {
			mapping.getValues().clear();
			mapping.getValues().addAll(userIds);
			userGroupMappingRepository.save(mapping);
		}
	}
	
	private void deleteUserGroup(UserGroup userGroup) {
		userGroupMappingRepository.delete(userGroup.getId());
	}
	
	private void mapUser(User user) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<UserGroup> userGroups = userGroupSource.findAll(user.getTenantId());
		for(UserGroup userGroup : userGroups) {
			final Mapping mapping = userGroupMappingRepository.findById(userGroup.getId());
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
		userGroupMappingRepository.saveAll(mappings);
	}
	
	private void deleteUser(User user) {
		final Collection<Mapping> mappings = new ArrayList<>();
		final Collection<UserGroup> userGroups = userGroupSource.findAll(user.getTenantId());
		for(UserGroup userGroup : userGroups) {
			final Mapping mapping = userGroupMappingRepository.findById(userGroup.getId());
			if(mapping.getValues().remove(user.getId())) {
				mappings.add(mapping);
			}
		}
		userGroupMappingRepository.saveAll(mappings);
	}
	
}
