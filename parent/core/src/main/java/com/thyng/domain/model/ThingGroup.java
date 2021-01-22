package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class ThingGroup implements TenantAwareModel {
	private static final long serialVersionUID = -8917862417622815872L;

	private String id;
	@NotBlank private String tenantId;
	@NotBlank private String script;
	@NotNull private Language language;
	@NotBlank @Size(max = 255) private String name;
}
