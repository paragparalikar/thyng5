package com.thyng.module;

import com.thyng.Context;
import com.thyng.domain.model.Action;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.ThingGroup;
import com.thyng.domain.model.ThingInfo;
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
		wrapThingInfoRepository(context);
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
					.eventBus(context.getEventBus())
					.entityName(Constant.TENANT)
					.build());
		}
	}
	
	private void wrapGatewayRepository(Context context) {
		final TenantAwareRepository<Gateway> delegate = context.getGatewayRepository();
		if(null != delegate) {
			context.setGatewayRepository(CacheTenantAwareRepository.<Gateway>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.GATEWAY)
					.build());
		}
	}
	
	private void wrapTemplateRepository(Context context) {
		final TenantAwareRepository<Template> delegate = context.getTemplateRepository();
		if(null != delegate) {
			context.setTemplateRepository(CacheTenantAwareRepository.<Template>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.TEMPALTE)
					.build());
		}
	}
	
	private void wrapThingRepository(Context context) {
		final TenantAwareRepository<Thing> delegate = context.getThingRepository();
		if(null != delegate) {
			context.setThingRepository(CacheTenantAwareRepository.<Thing>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.THING)
					.build());
		}
	}
	
	private void wrapThingInfoRepository(Context context) {
		final Repository<ThingInfo> delegate = context.getThingInfoRepository();
		if(null != delegate) {
			context.setThingInfoRepository(CacheRepository.<ThingInfo>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.THING_INFO)
					.build());
		}
	}

	private void wrapThingGroupRepository(Context context) {
		final TenantAwareRepository<ThingGroup> delegate = context.getThingGroupRepository();
		if(null != delegate) {
			context.setThingGroupRepository(CacheTenantAwareRepository.<ThingGroup>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.THING_GROUP)
					.build());
		}
	}
	
	private void wrapThingGroupMappingRepository(Context context) {
		final MappingRepository delegate = context.getThingGroupMappingRepository();
		if(null != delegate) {
			context.setThingGroupMappingRepository(CacheMappingRepository.builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.THING_GROUP_MAPPING)
					.build());
		}
	}
	
	private void wrapTriggerRepository(Context context) {
		final TenantAwareRepository<Trigger> delegate = context.getTriggerRepository();
		if(null != delegate) {
			context.setTriggerRepository(CacheTenantAwareRepository.<Trigger>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.TRIGGER)
					.build());
		}
	}
	
	private void wrapTriggerInfoRepository(Context context) {
		final Repository<TriggerInfo> delegate = context.getTriggerInfoRepository();
		if(null != delegate) {
			context.setTriggerInfoRepository(CacheRepository.<TriggerInfo>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.TRIGGER_INFO)
					.build());
		}
	}
	
	private void wrapActionRepository(Context context) {
		final TenantAwareRepository<Action> delegate = context.getActionRepository();
		if(null != delegate) {
			context.setActionRepository(CacheTenantAwareRepository.<Action>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.ACTION)
					.build());
		}
	}
	
	private void wrapUserRepository(Context context) {
		final TenantAwareRepository<User> delegate = context.getUserRepository();
		if(null != delegate) {
			context.setUserRepository(CacheTenantAwareRepository.<User>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.USER)
					.build());
		}
	}
	
	private void wrapUserGroupRepository(Context context) {
		final TenantAwareRepository<UserGroup> delegate = context.getUserGroupRepository();
		if(null != delegate) {
			context.setUserGroupRepository(CacheTenantAwareRepository.<UserGroup>builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.USER_GROUP)
					.build());
		}
	}
	
	private void wrapUserGroupMappingRepository(Context context) {
		final MappingRepository delegate = context.getUserGroupMappingRepository();
		if(null != delegate) {
			context.setUserGroupMappingRepository(CacheMappingRepository.builder()
					.delegate(delegate)
					.eventBus(context.getEventBus())
					.entityName(Constant.USER_GROUP_MAPPING)
					.build());
		}
	}
}
