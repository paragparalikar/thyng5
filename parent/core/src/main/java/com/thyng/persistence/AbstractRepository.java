package com.thyng.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.internal.util.StringUtil;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.thyng.domain.UnauthorizedActionException;
import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantContext;

public class AbstractRepository<T extends AbstractEntity> implements Repository<T, Integer> {
	
	private final IMap<Integer, T> cache;
	private final DynamoRepository<T> dynamoRepository;
	private final CacheRepository<T> cacheRepository;
	
	public AbstractRepository(String tableName, Class<T> type, DynamoDBMapper mapper, HazelcastInstance hazelcastInstance) {
		this.cache = hazelcastInstance.getMap(tableName);
		final String idProviderName = String.join("-", tableName, "id", "provider");
		final IAtomicLong idProvider = hazelcastInstance.getCPSubsystem().getAtomicLong(idProviderName);
		dynamoRepository = new DynamoRepository<>(mapper.newTableMapper(type));
		cacheRepository = new CacheRepository<>(cache, idProvider, dynamoRepository);
	}

	@PostConstruct
	public void initialize() {
		dynamoRepository.initialize();
		cacheRepository.initialize();
	}
	
	private T verifyTenant(T item) {
		final Integer tenantId = null == item ? null : item.getTenantId();
		final Tenant tenant = TenantContext.getTenant();
		if(null != tenantId && null != tenant && !tenantId.equals(tenant.getId())) {
			final String message = String.format("Can not operate with tenant id %d winthin "
					+ "context for tenant id %d", tenantId, tenant.getId());
			throw new UnauthorizedActionException(message);
		}
		return item;
	}
	
	private Predicate<Integer, T> tenantPredicate(){
		final Tenant tenant = TenantContext.getTenant();
		return null == tenant ? entry -> Boolean.TRUE : 
			entry -> tenant.getId().equals(AbstractEntity.class.cast(entry.getValue()).getTenantId());
	}
	
	public long count() {
		return cache.aggregate(Aggregators.count(), tenantPredicate());
	}

	public T getOne(Integer id) {
		return verifyTenant(cacheRepository.getOne(id));
	}

	public void delete(T item) {
		cacheRepository.delete(verifyTenant(item));
	}

	public List<T> findAll() {
		return new ArrayList<>(cache.values(tenantPredicate()));
	}
	
	protected List<T> findByPredicate(Predicate<Integer, T> predicate){
		final Predicate<Integer, T> tenantPredicate = tenantPredicate();
		final Predicate<Integer, T> compositePredicate = entry -> tenantPredicate.apply(entry) && predicate.apply(entry);
		return new ArrayList<>(cache.values(compositePredicate));
	}

	public T save(T item) {
		final Integer tenantId = item.getTenantId();
		final Tenant tenant = TenantContext.getTenant();
		if(null == tenantId && null == tenant) {
			throw new IllegalStateException("Tenant not available in item and context");
		} else if(null == tenantId && null != tenant) {
			item.setTenantId(tenant.getId());
		}
		return cacheRepository.save(verifyTenant(item));
	}

	public boolean existsByName(Integer id, String name) {
		final T entity = cache.get(id);
		final Tenant tenant = TenantContext.getTenant();
		return 0 < cache.aggregate(Aggregators.count(), entry -> existsByName(entity, entry.getValue(), tenant));
	}
	
	protected boolean existsByName(T entity, T other, Tenant tenant) {
		final Integer tenantId = null == tenant ? null : tenant.getId();
		final Integer otherId = null == other ? null : other.getId();
		final String otherName = null == other ? null : other.getName();
		final Integer otherTenantId = null == other ? null : other.getTenantId();
		final Integer entityId = null == entity ? null : entity.getId();
		final String entityName = null == entity ? null : entity.getName();
		final Integer entityTenantId = null == entity ? null : entity.getTenantId();
		
		if(null == tenantId) return !Objects.equals(entityId, otherId) 
				&& StringUtil.equalsIgnoreCase(entityName, otherName);
		
		else return tenantId.equals(entityTenantId) 
				&& tenantId.equals(otherTenantId)
				&& !Objects.equals(entityId, otherId) 
				&& StringUtil.equalsIgnoreCase(entityName, otherName);
	}
	
}