package com.thyng.repository;

import com.thyng.Callback;
import com.thyng.domain.intf.Lifecycle;

public interface CounterRepository extends Lifecycle {
	String CACHE_NAME = "counters";
	
	void get(String name, Callback<Long> callback);
	
	void addAndGet(String name, Long delta, Callback<Long> callback);

}
