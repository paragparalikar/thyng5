package com.thyng.repository.event;

import java.util.List;

import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.service.EventService;
import com.thyng.util.Constant;
import com.thyng.util.Strings;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@RequiredArgsConstructor
public class EventPublisherTenantAwareRepository<T extends TenantAwareModel<T>> implements TenantAwareRepository<T> {

	@NonNull private final String entityName;
	@NonNull private final EventService eventService;
	@NonNull private final TenantAwareRepository<T> delegate;
	
	@Override
	public List<T> findAll() {
		return delegate.findAll();
	}

	@Override
	public List<T> findAll(String tenantId) {
		return delegate.findAll(tenantId);
	}

	@Override
	public T save(T entity) {
		final String topic = Strings.isBlank(entity.getId()) ? 
				Constant.createdTopic(entityName) : Constant.updatedTopic(entityName);
		final T result = delegate.save(entity);
		eventService.publish(topic, result);
		return result;
	}

	@Override
	public T findById(String id) {
		return delegate.findById(id);
	}

	@Override
	public T findById(String tenantId, String id) {
		return delegate.findById(tenantId, id);
	}

	@Override
	public T deleteById(String tenantId, String id) {
		final T result = delegate.deleteById(tenantId, id);
		eventService.publish(Constant.deletedTopic(entityName), result);
		return result;
	}

	@Override
	public boolean existsByName(String tenantId, String id, String name) {
		return delegate.existsByName(tenantId, id, name);
	}

}
