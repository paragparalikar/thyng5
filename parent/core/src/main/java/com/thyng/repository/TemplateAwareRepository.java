package com.thyng.repository;

import java.util.List;

import com.thyng.domain.intf.TemplateAwareModel;

public interface TemplateAwareRepository<T extends TemplateAwareModel> extends TenantAwareRepository<T> {

	List<T> findByTemplateId(String tenantId, String templateId);
	
}
