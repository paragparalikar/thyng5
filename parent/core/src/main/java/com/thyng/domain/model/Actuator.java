package com.thyng.domain.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;

import lombok.Data;

@Data
public class Actuator implements Identifiable<String>, Nameable, Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotBlank @Size(max = 255) private String name;

}
