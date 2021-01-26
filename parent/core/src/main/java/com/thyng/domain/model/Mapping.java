package com.thyng.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import com.thyng.domain.intf.Identifiable;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class Mapping implements Serializable, Identifiable<Mapping> {
	private static final long serialVersionUID = 4951931148342631093L;
	
	private final String id;
	@Builder.Default private final Set<String> values = Collections.emptySet();
	
}
