package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.domain.enumeration.ActionType;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.Data;

@Data
public class Action implements TenantAwareModel {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotBlank private String tenantId;
	@NotNull private Boolean enabled;
	@NotNull private ActionType actionType;
	@NotBlank @Size(min = 3, max = 255) private String name;

}
