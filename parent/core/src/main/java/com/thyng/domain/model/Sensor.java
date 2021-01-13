package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.Data;

@Data
public class Sensor implements TenantAwareModel {
	private static final long serialVersionUID = 1L;
	public static final String CACHE_NAME = "sensor";
	
	private String id;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotBlank @Size(min = 1, max = 255) private String unit;
	@NotBlank private String tenantId;
	@NotBlank private String templateId;

}
