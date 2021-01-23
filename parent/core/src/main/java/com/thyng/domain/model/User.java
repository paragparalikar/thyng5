package com.thyng.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.thyng.domain.intf.TenantAwareModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User implements TenantAwareModel {
	private static final long serialVersionUID = 2764907017660209767L;

	private String id;
	@NotBlank private String name;
	@NotBlank private String tenantId;
	@NotBlank private String firstName;
	private String lastName;
	@NotBlank private String email;
	private String phone;
	private final Set<@Valid Attribute> attributes = new HashSet<>();
}
