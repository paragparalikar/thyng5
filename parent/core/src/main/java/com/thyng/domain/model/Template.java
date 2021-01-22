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
public class Template implements TenantAwareModel {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotBlank private String tenantId;
	@NotBlank @Size(max = 255) private String name;
	@NotNull @Min(60) private Long inactivityPeriod;
	
	private final Set<@Valid Sensor> sensors = new HashSet<>();
	private final Set<@Valid Actuator> actuators = new HashSet<>();
	private final Set<@Valid Attribute> attributes = new HashSet<>();
}
