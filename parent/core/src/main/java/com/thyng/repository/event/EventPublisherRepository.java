package com.thyng.repository.event;

import java.util.List;

import com.thyng.domain.intf.Identifiable;
import com.thyng.repository.Repository;
import com.thyng.service.EventService;
import com.thyng.util.Constant;
import com.thyng.util.Strings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class EventPublisherRepository<T extends Identifiable<T>> implements Repository<T> {

	@NonNull private final String entityName;
	@NonNull private final Repository<T> delegate;
	@NonNull private final EventService eventService;
	
	@Override
	public List<T> findAll() {
		return delegate.findAll();
	}

	@Override
	public T save(T entity) {
		final String topic = Strings.isBlank(entity.getId()) ? Constant.createdTopic(entityName) : Constant.updatedTopic(entityName);
		final T result = delegate.save(entity);
		eventService.publish(topic, result);
		return result;
	}

	@Override
	public T findById(String id) {
		return delegate.findById(id);
	}

	@Override
	public T deleteById(String id) {
		final T result = delegate.deleteById(id);
		eventService.publish(Constant.deletedTopic(entityName), result);
		return result;
	}

}
