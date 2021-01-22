package com.thyng.domain.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute implements Identifiable<String>, Nameable, Serializable {
	private static final long serialVersionUID = -8647679406655187521L;

	private String id;
	@NotBlank @Size(max = 255) private String name;
	@NotBlank @Size(max = 255) private String value;
	
}
