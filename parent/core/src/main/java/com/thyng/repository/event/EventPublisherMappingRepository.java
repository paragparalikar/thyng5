package com.thyng.repository.event;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.thyng.domain.model.Mapping;
import com.thyng.repository.MappingRepository;
import com.thyng.service.EventService;
import com.thyng.util.Constant;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class EventPublisherMappingRepository implements MappingRepository {

	@NonNull private final String entityName;
	@NonNull private final EventService eventService;
	@NonNull private final MappingRepository delegate;
	
	@Override
	public List<Mapping> findAll() {
		return delegate.findAll();
	}
	@Override
	public CompletableFuture<Mapping> save(Mapping mapping) {
		return delegate.save(mapping)
				.thenApply(result -> {
					eventService.publish(Constant.createdTopic(entityName), result);
					return result;
				});
	}
	@Override
	public CompletableFuture<Collection<Mapping>> saveAll(Collection<Mapping> mappings) {
		return delegate.saveAll(mappings)
				.thenApply(results -> {
					results.forEach(result -> {
						eventService.publish(Constant.createdTopic(entityName), result);
					});
					return results;
				});
	}
	@Override
	public CompletableFuture<String> delete(String id) {
		return delegate.delete(id)
				.thenApply(result -> {
					eventService.publish(Constant.deletedTopic(entityName), result);
					return result;
				});
	}
	
}
