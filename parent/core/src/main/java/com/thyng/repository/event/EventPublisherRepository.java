package com.thyng.repository.event;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.thyng.domain.intf.Identifiable;
import com.thyng.event.EventBus;
import com.thyng.repository.Repository;
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
	@NonNull private final EventBus eventBus;
	
	private String resolveTopic(T entity) {
		return Strings.isBlank(entity.getId()) ? Constant.createdTopic(entityName) : Constant.updatedTopic(entityName);
	}
	
	@Override
	public List<T> findAll() {
		return delegate.findAll();
	}

	@Override
	public T save(T entity) {
		final String topic = resolveTopic(entity);
		final T result = delegate.save(entity);
		eventBus.publish(topic, result);
		return result;
	}
	
	@Override
	public <C extends Collection<T>> C saveAll(C entities) {
		final Set<String> updateIds = entities.stream()
			.map(Identifiable::getId)
			.filter(Strings::isNotBlank)
			.collect(Collectors.toSet());
		final C results = delegate.saveAll(entities);
		if(null != results) {
			results.forEach(result -> {
				final String topic = updateIds.contains(result.getId()) ? 
						Constant.updatedTopic(entityName) : Constant.createdTopic(entityName);
				eventBus.publish(topic, result);
			});
		}
		return results;
	}

	@Override
	public T findById(String id) {
		return delegate.findById(id);
	}

	@Override
	public T deleteById(String id) {
		final T result = delegate.deleteById(id);
		eventBus.publish(Constant.deletedTopic(entityName), result);
		return result;
	}
	
	@Override
	public <C extends Collection<T>> C deleteAll(C entities) {
		final C results = delegate.deleteAll(entities);
		results.forEach(result -> eventBus.publish(Constant.deletedTopic(entityName), result));
		return results;
	}

}
