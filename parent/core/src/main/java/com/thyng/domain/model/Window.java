package com.thyng.domain.model;

import java.io.Serializable;
import java.time.Duration;

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
	
	public long toMillis() {
		switch(type) {
		case TUMBLING: return Duration.ofMinutes(span).toMillis();
		case SLIDING: return Duration.ofMinutes(slidingSpan).toMillis();
		case SESSION: return Duration.ofMinutes(timeout).toMillis();
		default: return 0;
		}
	}

}
