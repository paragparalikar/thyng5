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
import com.hazelcast.internal.util.StringUtil;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.thyng.Callback;
import com.thyng.domain.exception.UnauthorizedActionException;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.repository.MultiTenantRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class MultiTenantHazelcastRepository<T extends TenantAwareModel> implements MultiTenantRepository<T> {

	private IMap<String, T> cache;
	private IAtomicLong idProvider;
	
	private final String cacheName;
	private final MultiTenantRepository<T> delegate;
	private final HazelcastInstance hazelcastInstance;
	
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
			final CountDownLatch latch = new CountDownLatch(1);
			delegate.findAll(Callback.<List<T>>builder()
				.success(this::cache)
				.after(() -> latch.countDown())
				.build());
			latch.await();
			lock.unlock();
		}
	}
	
	protected void cache(List<T> items) {
		final AtomicLong maxId = new AtomicLong(0);
		items.forEach(item -> {
			cache.put(item.getId(), item);
			final long id = Long.parseUnsignedLong(item.getId(), Character.MAX_RADIX);
			maxId.updateAndGet(value -> Math.max(value, id));
		});
		final long maxIdValue = maxId.get();
		idProvider.alter(value -> Math.max(value, maxIdValue));
	}
	
	protected String nextId() {
		return Long.toString(idProvider.incrementAndGet(), Character.MAX_RADIX);
	}
	
	private Predicate<String, T> tenantPredicate(String tenantId){
		return null == tenantId ? entry -> Boolean.TRUE : entry -> tenantId.equals(entry.getValue().getTenantId());
	}
	
	protected List<T> findByPredicate(String tenantId, Predicate<String, T> predicate){
		final Predicate<String, T> tenantPredicate = tenantPredicate(tenantId);
		final Predicate<String, T> compositePredicate = entry -> tenantPredicate.apply(entry) && predicate.apply(entry);
		return new ArrayList<>(cache.values(compositePredicate));
	}
	
	private <S extends T> S verifyTenant(String tenantId, S item) {
		final String itemTenantId = null == item ? null : item.getTenantId();
		if(null != itemTenantId && !itemTenantId.equals(tenantId)) {
			final String message = String.format("Can not operate with tenant id %d winthin "
					+ "context for tenant id %d", itemTenantId, tenantId);
			throw new UnauthorizedActionException(message);
		}
		return item;
	}
	
	@Override
	public void count(String tenantId, Callback<Long> callback) {
		callback.call(cache.aggregate(Aggregators.count(), tenantPredicate(tenantId)), null);		
	}
	
	@Override
	public void findById(String tenantId, String id, Callback<Optional<T>> callback) {
		final T item = verifyTenant(tenantId, cache.get(id));
		callback.call(Optional.ofNullable(item), null);
	}
	
	@Override
	public void deleteById(String tenantId, String id, Callback<T> callback) {
		verifyTenant(tenantId, cache.get(id));
		delegate.deleteById(tenantId, id, Callback.<T>builder()
				.after(callback.getAfter())
				.failure(callback.getFailure())
				.success(value -> {
					cache.remove(id);
					callback.getSuccess().accept(value);
				})
				.build());
	}

	@Override
	public void save(T entity, Callback<T> callback) {
		if(null == entity.getId() || 0 == entity.getId().trim().length() || "0".equals(entity.getId())) {
			entity.setId(nextId());
		}
		delegate.save(entity, Callback.<T>builder()
				.failure(callback.getFailure())
				.after(callback.getAfter())
				.success(item -> {
					cache.put(item.getId(), item);
					callback.getSuccess().accept(item);
				})
				.build());
	}
	
	@Override
	public void findAll(Callback<List<T>> callback) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void findAll(String tenantId, Callback<List<T>> callback) {
		callback.call(new ArrayList<>(cache.values(tenantPredicate(tenantId))), null);
	}

	@Override
	public void existsByName(String tenantId, String id, String name, Callback<Boolean> callback) {
		final T entity = cache.get(id);
		final boolean result = 0 < cache.aggregate(Aggregators.count(), entry -> existsByName(entity, entry.getValue()));
		callback.call(result, null);
	}
	
	protected boolean existsByName(T entity, T other) {
		final String otherId = null == other ? null : other.getId();
		final String otherName = null == other ? null : other.getName();
		final String otherTenantId = null == other ? null : other.getTenantId();
		final String entityId = null == entity ? null : entity.getId();
		final String entityName = null == entity ? null : entity.getName();
		final String entityTenantId = null == entity ? null : entity.getTenantId();
		return entityTenantId.equals(otherTenantId) 
				&& !Objects.equals(entityId, otherId) 
				&& StringUtil.equalsIgnoreCase(entityName.trim(), otherName.trim());
	}
}
