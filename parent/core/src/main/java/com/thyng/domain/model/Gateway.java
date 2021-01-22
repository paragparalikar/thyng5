package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Gateway implements TenantAwareModel {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotBlank @Size(max = 255) private String name;
	@NotBlank private String tenantId;
	private String publicKey;
	private String privateKey;

}
