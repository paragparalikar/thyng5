package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.domain.enumeration.ActionType;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Action implements TenantAwareModel {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotBlank private String tenantId;
	@NotNull private Boolean enabled;
	@NotNull private ActionType actionType;
	@NotBlank @Size(max = 255) private String name;

}
