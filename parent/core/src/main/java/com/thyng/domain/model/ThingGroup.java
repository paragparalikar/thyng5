package com.thyng.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.domain.enumeration.Language;
import com.thyng.domain.enumeration.ThingGroupType;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.Data;

@Data
public class ThingGroup implements TenantAwareModel {
	private static final long serialVersionUID = -8917862417622815872L;

	private String id;
	@NotBlank private String tenantId;
	@NotNull private ThingGroupType type;
	@NotBlank @Size(max = 255) private String name;

	private String script;
	private Language language;
	private final Set<String> thingIds = new HashSet<>();
	private final Set<String> templateIds = new HashSet<>();
	
}
