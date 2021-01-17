package com.thyng.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.domain.enumeration.EventType;
import com.thyng.domain.enumeration.Language;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.Data;

@Data
public class Trigger implements TenantAwareModel {
	private static final long serialVersionUID = 1L;

	private String id;
	@Valid private Window window;
	@NotBlank private String tenantId;
	@NotNull private Boolean enabled = Boolean.TRUE;
	@NotNull private Language language;
	@NotNull private EventType eventType;
	@NotBlank private String thingSelectionScript;
	@NotBlank private String evaluationScript;
	@NotBlank @Size(min = 3, max = 255) private String name;

}
