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
public class Tenant implements Identifiable<Tenant>, Nameable, Serializable {
	private static final long serialVersionUID = 1L;

	private final String id;
	private final String name;
	@Builder.Default private final Boolean enabled = Boolean.TRUE;

}
