package com.thyng.module;

import com.thyng.Context;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.Trigger;
import com.thyng.domain.model.TriggerInfo;
import com.thyng.domain.model.User;
import com.thyng.domain.model.UserGroup;
import com.thyng.repository.MappingRepository;
import com.thyng.repository.NameableRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.repository.cache.CacheMappingRepository;
import com.thyng.repository.cache.CacheNameableRepository;
import com.thyng.repository.cache.CacheRepository;
import com.thyng.repository.cache.CacheTenantAwareRepository;
import com.thyng.util.Constant;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class CacheRepositoryModule implements Module {
	
	@Getter private final int order = Constant.ORDER_MODULE_CACHE_MAP;
	@Setter @NonNull private Context context;
	
	@Override
	public void start() throws Exception {
		wrapTenantRepository(context);
		wrapGatewayRepository(context);
		wrapTemplateRepository(context);
		wrapThingRepository(context);
		wrapThingGroupRepository(context);
		wrapThingGroupMappingRepository(context);
		wrapTriggerRepository(context);
		wrapTriggerInfoRepository(context);
		wrapActionRepository(context);
		wrapUserRepository(context);
		wrapUserGroupRepository(context);
		wrapUserGroupMappingRepository(context);
	}
	
	private void wrapTenantRepository(Context context) {
		final NameableRepository<Tenant> tenantRepository = context.getTenantRepository();
		if(null != tenantRepository) {
			context.setTenantRepository(CacheNameableRepository.<Tenant>nameableRepositoryBuilder()
					.delegate(tenantRepository)
					.eventService(context.getEventService())
					.createdTopic(Constant.TENANT_CREATED)
					.updatedTopic(Constant.TENANT_UPDATED)
					.deletedTopic(Constant.TENANT_DELETED)
					.build());
		}
	}
	
	private void wrapGatewayRepository(Context context) {
		final TenantAwareRepository<Gateway> delegate = context.getGatewayRepository();
		if(null != delegate) {
			context.setGatewayRepository(CacheTenantAwareRepository.<Gateway>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.GATEWAY_CREATED)
					.updatedTopic(Constant.GATEWAY_UPDATED)
					.deletedTopic(Constant.GATEWAY_DELETED)
					.build());
		}
	}
	
	private void wrapTemplateRepository(Context context) {
		final TenantAwareRepository<Template> delegate = context.getTemplateRepository();
		if(null != delegate) {
			context.setTemplateRepository(CacheTenantAwareRepository.<Template>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.TEMPLATE_CREATED)
					.updatedTopic(Constant.TEMPLATE_UPDATED)
					.deletedTopic(Constant.TEMPLATE_DELETED)
					.build());
		}
	}
	
	private void wrapThingRepository(Context context) {
		final TenantAwareRepository<Thing> delegate = context.getThingRepository();
		if(null != delegate) {
			context.setThingRepository(CacheTenantAwareRepository.<Thing>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.THING_CREATED)
					.updatedTopic(Constant.THING_UPDATED)
					.deletedTopic(Constant.THING_DELETED)
					.build());
		}
	}

	private void wrapThingGroupRepository(Context context) {
		final TenantAwareRepository<ThingGroup> delegate = context.getThingGroupRepository();
		if(null != delegate) {
			context.setThingGroupRepository(CacheTenantAwareRepository.<ThingGroup>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.THING_GROUP_CREATED)
					.updatedTopic(Constant.THING_GROUP_UPDATED)
					.deletedTopic(Constant.THING_GROUP_DELETED)
					.build());
		}
	}
	
	private void wrapThingGroupMappingRepository(Context context) {
		final MappingRepository delegate = context.getThingGroupMappingRepository();
		if(null != delegate) {
			context.setThingGroupMappingRepository(CacheMappingRepository.builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.THING_GROUP_MAPPING_CREATED)
					.updatedTopic(Constant.THING_GROUP_MAPPING_UPDATED)
					.deletedTopic(Constant.THING_GROUP_MAPPING_DELETED)
					.build());
		}
	}
	
	private void wrapTriggerRepository(Context context) {
		final TenantAwareRepository<Trigger> delegate = context.getTriggerRepository();
		if(null != delegate) {
			context.setTriggerRepository(CacheTenantAwareRepository.<Trigger>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.TRIGGER_CREATED)
					.updatedTopic(Constant.TRIGGER_UPDATED)
					.deletedTopic(Constant.TRIGGER_DELETED)
					.build());
		}
	}
	
	private void wrapTriggerInfoRepository(Context context) {
		final Repository<TriggerInfo> delegate = context.getTriggerInfoRepository();
		if(null != delegate) {
			context.setTriggerInfoRepository(CacheRepository.<TriggerInfo>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.TRIGGER_INFO_CREATED)
					.updatedTopic(Constant.TRIGGER_INFO_UPDATED)
					.deletedTopic(Constant.TRIGGER_INFO_DELETED)
					.build());
		}
	}
	
	private void wrapActionRepository(Context context) {
		final TenantAwareRepository<Action> delegate = context.getActionRepository();
		if(null != delegate) {
			context.setActionRepository(CacheTenantAwareRepository.<Action>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.ACTION_CREATED)
					.updatedTopic(Constant.ACTION_UPDATED)
					.deletedTopic(Constant.ACTION_DELETED)
					.build());
		}
	}
	
	private void wrapUserRepository(Context context) {
		final TenantAwareRepository<User> delegate = context.getUserRepository();
		if(null != delegate) {
			context.setUserRepository(CacheTenantAwareRepository.<User>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.USER_CREATED)
					.updatedTopic(Constant.USER_UPDATED)
					.deletedTopic(Constant.USER_DELETED)
					.build());
		}
	}
	
	private void wrapUserGroupRepository(Context context) {
		final TenantAwareRepository<UserGroup> delegate = context.getUserGroupRepository();
		if(null != delegate) {
			context.setUserGroupRepository(CacheTenantAwareRepository.<UserGroup>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.USER_GROUP_CREATED)
					.updatedTopic(Constant.USER_GROUP_UPDATED)
					.deletedTopic(Constant.USER_GROUP_DELETED)
					.build());
		}
	}
	
	private void wrapUserGroupMappingRepository(Context context) {
		final MappingRepository delegate = context.getUserGroupMappingRepository();
		if(null != delegate) {
			context.setUserGroupMappingRepository(CacheMappingRepository.builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.createdTopic(Constant.USER_GROUP_MAPPING_CREATED)
					.updatedTopic(Constant.USER_GROUP_MAPPING_UPDATED)
					.deletedTopic(Constant.USER_GROUP_MAPPING_DELETED)
					.build());
		}
	}
}
