package com.thyng.domain.intf;

public interface TemplateAware extends TenantAware {

	String getTemplateId();
	
	void setTemplateId(String templateId);
	
}
