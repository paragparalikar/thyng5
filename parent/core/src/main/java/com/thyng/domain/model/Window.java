package com.thyng.domain.model;

import java.io.Serializable;

import com.thyng.domain.enumeration.WindowBase;
import com.thyng.domain.enumeration.WindowType;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Value
@Builder
@Jacksonized
public class Window implements Serializable {
	private static final long serialVersionUID = 1L;

	private final WindowBase base;
	private final WindowType type;
	private Long timeout;
	private Long span;
	private Long slidingSpan;

}
