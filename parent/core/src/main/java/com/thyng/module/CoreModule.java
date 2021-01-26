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
import com.thyng.repository.MetricRepository;
import com.thyng.repository.NameableRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.repository.event.EventPublisherMappingRepository;
import com.thyng.repository.event.EventPublisherNameableRepository;
import com.thyng.repository.event.EventPublisherRepository;
import com.thyng.repository.event.EventPublisherTenantAwareRepository;
import com.thyng.service.MetricService;
import com.thyng.util.Constant;

import lombok.Getter;
import lombok.Setter;

public class CoreModule implements Module {
	
	@Setter private Context context;
	@Getter private int order = Constant.ORDER_MODULE_CORE;
	
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
		
		final MetricRepository metricRepository = context.getMetricRepository();
		if(null != metricRepository) {
			context.setMetricService(new MetricService(metricRepository));
		}
	}

	private void wrapTenantRepository(Context context) {
		final NameableRepository<Tenant> tenantRepository = context.getTenantRepository();
		if(null != tenantRepository) {
			context.setTenantRepository(EventPublisherNameableRepository.<Tenant>nameableRepositoryBuilder()
					.delegate(tenantRepository)
					.eventService(context.getEventService())
					.entityName(Constant.TENANT)
					.build());
		}
	}
	
	private void wrapGatewayRepository(Context context) {
		final TenantAwareRepository<Gateway> delegate = context.getGatewayRepository();
		if(null != delegate) {
			context.setGatewayRepository(EventPublisherTenantAwareRepository.<Gateway>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.GATEWAY)
					.build());
		}
	}
	
	private void wrapTemplateRepository(Context context) {
		final TenantAwareRepository<Template> delegate = context.getTemplateRepository();
		if(null != delegate) {
			context.setTemplateRepository(EventPublisherTenantAwareRepository.<Template>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.TEMPALTE)
					.build());
		}
	}
	
	private void wrapThingRepository(Context context) {
		final TenantAwareRepository<Thing> delegate = context.getThingRepository();
		if(null != delegate) {
			context.setThingRepository(EventPublisherTenantAwareRepository.<Thing>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.THING)
					.build());
		}
	}

	private void wrapThingGroupRepository(Context context) {
		final TenantAwareRepository<ThingGroup> delegate = context.getThingGroupRepository();
		if(null != delegate) {
			context.setThingGroupRepository(EventPublisherTenantAwareRepository.<ThingGroup>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.THING_GROUP)
					.build());
		}
	}
	
	private void wrapThingGroupMappingRepository(Context context) {
		final MappingRepository delegate = context.getThingGroupMappingRepository();
		if(null != delegate) {
			context.setThingGroupMappingRepository(EventPublisherMappingRepository.builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.THING_GROUP_MAPPING)
					.build());
		}
	}
	
	private void wrapTriggerRepository(Context context) {
		final TenantAwareRepository<Trigger> delegate = context.getTriggerRepository();
		if(null != delegate) {
			context.setTriggerRepository(EventPublisherTenantAwareRepository.<Trigger>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.TRIGGER)
					.build());
		}
	}
	
	private void wrapTriggerInfoRepository(Context context) {
		final Repository<TriggerInfo> delegate = context.getTriggerInfoRepository();
		if(null != delegate) {
			context.setTriggerInfoRepository(EventPublisherRepository.<TriggerInfo>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.TRIGGER_INFO)
					.build());
		}
	}
	
	private void wrapActionRepository(Context context) {
		final TenantAwareRepository<Action> delegate = context.getActionRepository();
		if(null != delegate) {
			context.setActionRepository(EventPublisherTenantAwareRepository.<Action>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.ACTION)
					.build());
		}
	}
	
	private void wrapUserRepository(Context context) {
		final TenantAwareRepository<User> delegate = context.getUserRepository();
		if(null != delegate) {
			context.setUserRepository(EventPublisherTenantAwareRepository.<User>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.USER)
					.build());
		}
	}
	
	private void wrapUserGroupRepository(Context context) {
		final TenantAwareRepository<UserGroup> delegate = context.getUserGroupRepository();
		if(null != delegate) {
			context.setUserGroupRepository(EventPublisherTenantAwareRepository.<UserGroup>builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.USER_GROUP)
					.build());
		}
	}
	
	private void wrapUserGroupMappingRepository(Context context) {
		final MappingRepository delegate = context.getUserGroupMappingRepository();
		if(null != delegate) {
			context.setUserGroupMappingRepository(EventPublisherMappingRepository.builder()
					.delegate(delegate)
					.eventService(context.getEventService())
					.entityName(Constant.USER_GROUP_MAPPING)
					.build());
		}
	}
}
