package com.thyng.domain.tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.util.StringUtil;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.thyng.domain.UnauthorizedActionException;
import com.thyng.persistence.CacheRepository;
import com.thyng.persistence.DynamoRepository;

import lombok.NonNull;

public class TenantAwareRepository<T extends TenantAwareEntity> extends CacheRepository<T> {
	
	private IMap<Long, T> cache;
	
	public TenantAwareRepository(String tableName, Class<T> type, DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		super(tableName, new DynamoRepository<>(mapper.newTableMapper(type)), hazelcastInstance);
	}
	
	@Override
	public void initialize() {
		super.initialize();
		this.cache = super.getCache();
	}
	
	private T verifyTenant(T item) {
		final Long tenantId = null == item ? null : item.getTenantId();
		final Tenant tenant = TenantContext.getTenant();
		if(null != tenantId && null != tenant && !tenantId.equals(tenant.getId())) {
			final String message = String.format("Can not operate with tenant id %d winthin "
					+ "context for tenant id %d", tenantId, tenant.getId());
			throw new UnauthorizedActionException(message);
		}
		return item;
	}
	
	private Predicate<Long, T> tenantPredicate(){
		final Tenant tenant = TenantContext.getTenant();
		return null == tenant ? entry -> Boolean.TRUE : 
			entry -> tenant.getId().equals(TenantAwareEntity.class.cast(entry.getValue()).getTenantId());
	}
	
	public long count() {
		return cache.aggregate(Aggregators.count(), tenantPredicate());
	}

	public T getOne(@NonNull Long id) {
		return verifyTenant(super.getOne(id));
	}

	public void delete(@NonNull T item) {
		super.delete(verifyTenant(item));
	}

	public List<T> findAll() {
		return new ArrayList<>(cache.values(tenantPredicate()));
	}
	
	protected List<T> findByPredicate(Predicate<Long, T> predicate){
		final Predicate<Long, T> tenantPredicate = tenantPredicate();
		final Predicate<Long, T> compositePredicate = entry -> tenantPredicate.apply(entry) && predicate.apply(entry);
		return new ArrayList<>(cache.values(compositePredicate));
	}

	public T save(@NonNull T item) {
		final Long tenantId = item.getTenantId();
		final Tenant tenant = TenantContext.getTenant();
		if(null == tenantId && null == tenant) {
			throw new IllegalStateException("Tenant not available in item and context");
		} else if(null == tenantId && null != tenant) {
			item.setTenantId(tenant.getId());
		}
		return super.save(verifyTenant(item));
	}

	public boolean existsByName(@NonNull Long id,@NonNull String name) {
		final T entity = cache.get(id);
		final Tenant tenant = TenantContext.getTenant();
		return 0 < cache.aggregate(Aggregators.count(), entry -> existsByName(entity, entry.getValue(), tenant));
	}
	
	protected boolean existsByName(T entity, T other, Tenant tenant) {
		final Long tenantId = null == tenant ? null : tenant.getId();
		final Long otherId = null == other ? null : other.getId();
		final String otherName = null == other ? null : other.getName();
		final Long otherTenantId = null == other ? null : other.getTenantId();
		final Long entityId = null == entity ? null : entity.getId();
		final String entityName = null == entity ? null : entity.getName();
		final Long entityTenantId = null == entity ? null : entity.getTenantId();
		
		if(null == tenantId) return !Objects.equals(entityId, otherId) 
				&& StringUtil.equalsIgnoreCase(entityName, otherName);
		
		else return tenantId.equals(entityTenantId) 
				&& tenantId.equals(otherTenantId)
				&& !Objects.equals(entityId, otherId) 
				&& StringUtil.equalsIgnoreCase(entityName, otherName);
	}
	
}