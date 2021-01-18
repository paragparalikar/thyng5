package com.thyng.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.Data;

@Data
public class Template implements TenantAwareModel {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotBlank private String tenantId;
	@NotBlank @Size(max = 255) private String name;
	@NotNull @Min(60) private Integer inactivityPeriod;
	
	private Set<@Valid Sensor> sensors = new HashSet<>();
	private Set<@Valid Actuator> actuators = new HashSet<>();
	private Set<@Valid Attribute> attributes = new HashSet<>();
}
