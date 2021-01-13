package com.thyng.domain.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;

import lombok.Data;

@Data
public class Tenant implements Identifiable<String>, Nameable, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String CACHE_NAME = "tenant";

	private String id;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotBlank private Boolean enabled = Boolean.TRUE;

}
