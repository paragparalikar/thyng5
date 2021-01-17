package com.thyng.repository;

import com.thyng.domain.intf.Lifecycle;

public interface CounterRepository extends Lifecycle {
	
	long get(String name);
	
	long addAndGet(String name, Long delta);

}
