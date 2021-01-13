package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.Data;

@Data
public class Gateway implements TenantAwareModel {
	private static final long serialVersionUID = 1L;
	public static final String CACHE_NAME = "gateway";
	
	private String id;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotBlank private String tenantId;
	private String publicKey;
	private String privateKey;

}
