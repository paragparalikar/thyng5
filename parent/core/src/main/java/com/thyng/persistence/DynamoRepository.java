package com.thyng.persistence;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DynamoRepository<T extends Identifiable<Integer> & Nameable> implements Repository<T, Integer> {

	@Getter
	protected final DynamoDBTableMapper<T, Integer, ?> mapper;
	
	public void initialize() {
		mapper.createTableIfNotExists(new ProvisionedThroughput(5L,5L));
	}

	@Override
	public long count() {
		return mapper.count(new DynamoDBScanExpression().withSelect(Select.COUNT));
	}

	@Override
	public T getOne(Integer id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(T item) {
		mapper.delete(item);
	}

	@Override
	public List<T> findAll() {
		return mapper.scan(new DynamoDBScanExpression().withSelect(Select.ALL_ATTRIBUTES));
	}

	@Override
	public T save(T item) {
		mapper.save(item);
		return item;
	}
	
	@Override
	public boolean existsByName(Integer id, String name) {
		throw new UnsupportedOperationException();
	}
	
}
