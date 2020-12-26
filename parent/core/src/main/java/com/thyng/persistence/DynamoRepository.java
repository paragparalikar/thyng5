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
public class DynamoRepository<T extends Identifiable<String> & Nameable> implements Repository<T, String> {

	@Getter
	protected final DynamoDBTableMapper<T, String, ?> mapper;
	
	public void initialize() {
		mapper.createTableIfNotExists(new ProvisionedThroughput(5L,5L));
	}

	@Override
	public long count() {
		return mapper.count(new DynamoDBScanExpression().withSelect(Select.COUNT));
	}

	@Override
	public T getOne(String id) {
		return mapper.load(id);
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
		if(null != item.getId() && 0 == item.getId().trim().length()) item.setId(null);
		mapper.save(item);
		return item;
	}
	
	@Override
	public boolean existsByName(String id, String name) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
