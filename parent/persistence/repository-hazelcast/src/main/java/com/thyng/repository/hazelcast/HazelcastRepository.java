package com.thyng.repository.hazelcast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;
import com.thyng.Callback;
import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Nameable;
import com.thyng.repository.Repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

//@Slf4j
@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class HazelcastRepository<T extends Identifiable<String> & Nameable> implements Lifecycle, Repository<T, String> {

	private IMap<String, T> cache;
	private IAtomicLong idProvider;
	
	@NonNull private final String cacheName;
	@NonNull private final Repository<T, String> delegate;
	@NonNull private final HazelcastInstance hazelcastInstance;
	
	@Override
	public void start() throws Exception {
		this.cache = hazelcastInstance.getMap(cacheName);
		this.idProvider = hazelcastInstance.getCPSubsystem().getAtomicLong(HazelcastNames.idGenerator(cacheName));
		load();
	}
	
	@SneakyThrows
	protected void load() {
		final FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(HazelcastNames.lock(cacheName));
		if(cache.isEmpty() && lock.tryLock()) {
			final AtomicLong maxId = new AtomicLong(0);
			final CountDownLatch latch = new CountDownLatch(1);
			delegate.findAll(Callback.<List<T>>builder()
					.success(items -> {
						items.forEach(item -> {
							cache.put(item.getId(), item);
							final long id = Long.parseUnsignedLong(item.getId(), Character.MAX_RADIX);
							maxId.updateAndGet(value -> Math.max(value, id));
						});
						final long maxIdValue = maxId.get();
						idProvider.alter(value -> Math.max(value, maxIdValue));
					})
					.after(() -> {
						latch.countDown();
					})
					.build());
			latch.await();
			lock.unlock();
			lock.destroy();
		}
	}
	
	protected String nextId() {
		return Long.toString(idProvider.incrementAndGet(), Character.MAX_RADIX);
	}
	
	@Override
	public void count(Callback<Long> callback) {
		callback.call(new Long(cache.size()), null);
	}
	
	@Override
	public void deleteById(String id, Callback<T> callback) {
		delegate.deleteById(id, Callback.<T>builder()
				.success(item -> {
					Optional.ofNullable(item).ifPresent(i -> cache.remove(item.getId()));
					callback.getSuccess().accept(item);
				})
				.failure(callback.getFailure())
				.after(callback.getAfter())
				.build());
	}
	
	@Override
	public void findAll(Callback<List<T>> callback) {
		callback.call(new ArrayList<>(cache.values()), null);
	}
	
	@Override
	public void findById(String id, Callback<Optional<T>> callback) {
		callback.call(Optional.ofNullable(cache.get(id)), null);
	}
	
	public void save(T entity, Callback<T> callback) {
		if(null == entity.getId() || 0 == entity.getId().trim().length() || "0".equals(entity.getId())) {
			entity.setId(nextId());
		}
		delegate.save(entity, Callback.<T>builder()
				.success(item -> {
					cache.put(item.getId(), item);
					callback.getSuccess().accept(item);
				})
				.failure(callback.getFailure())
				.after(callback.getAfter())
				.build());
	}
	
	@Override
	public void existsByName(String id, String name, Callback<Boolean> callback) {
		final boolean result = 0 < cache.aggregate(Aggregators.count(), entry -> {
			final T entity = entry.getValue();
			return !Objects.equals(id, entity.getId()) 
					&& name.trim().equalsIgnoreCase(entity.getName().trim());
		});
		callback.call(result, null);
	}

}
