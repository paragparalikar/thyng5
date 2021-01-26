package com.thyng.domain.model;

import java.io.Serializable;

import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class Attribute implements Identifiable<Attribute, String>, Nameable, Serializable {
	private static final long serialVersionUID = -8647679406655187521L;

	private final String id;
	private final String name;
	private final String value;
	
}
