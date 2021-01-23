package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.thyng.domain.enumeration.Language;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup implements TenantAwareModel {
	private static final long serialVersionUID = 4533362266165981673L;

	private String id;
	@NotBlank private String name;
	@NotBlank private String script;
	@NotBlank private String tenantId;
	@NotNull private Language language;

}
