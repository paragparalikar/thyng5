package com.thyng.repository;

import java.util.List;

import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.model.TriggerInfo;

public interface TriggerInfoRepository extends Lifecycle {
	
	List<TriggerInfo> findAll();
	
	void save(TriggerInfo triggerInfo);
	
}
