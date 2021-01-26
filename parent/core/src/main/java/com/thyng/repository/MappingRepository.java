package com.thyng.repository;

import java.util.Collection;
import java.util.List;

import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Mapping;

public interface MappingRepository extends Lifecycle {
	
	List<Mapping> findAll();
	
	void save(Mapping mapping, Callback<Mapping> callback);
	
	void saveAll(Collection<Mapping> mappings, Callback<Collection<Mapping>> callback);
	
	void delete(String id, Callback<String> callback);

}
