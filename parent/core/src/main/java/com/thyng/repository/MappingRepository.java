package com.thyng.repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.Mapping;

public interface MappingRepository extends Lifecycle {
	
	List<Mapping> findAll();
	
	CompletableFuture<Mapping> save(Mapping mapping);
	
	CompletableFuture<Collection<Mapping>> saveAll(Collection<Mapping> mappings);
	
	CompletableFuture<String> delete(String id);

}
