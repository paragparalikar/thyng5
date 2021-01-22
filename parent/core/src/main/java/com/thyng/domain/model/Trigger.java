package com.thyng.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.domain.enumeration.EventType;
import com.thyng.domain.enumeration.Language;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Trigger implements TenantAwareModel {
	private static final long serialVersionUID = 1L;

	private String id;
	@Valid private Window window;
	@NotBlank private String script;
	@NotBlank private String tenantId;
	@NotNull private Language language;
	@NotNull private EventType eventType;
	@NotBlank @Size(max = 255) private String name;
	@Builder.Default @NotNull private Boolean enabled = Boolean.TRUE;
	private final Set<@NotBlank String> thingGroupIds = new HashSet<>();

}
