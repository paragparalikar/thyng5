package com.thyng.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class Thing implements TenantAwareModel {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotBlank @Size(max = 255) private String name;
	@NotBlank private String tenantId;
	@NotBlank private String templateId;
	@NotNull @Min(60) private Long inactivityPeriod;
	private final Set<@Valid Attribute> attributes = new HashSet<>();

}
