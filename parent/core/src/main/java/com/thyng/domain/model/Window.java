package com.thyng.domain.model;

import java.io.Serializable;
import java.time.Duration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.thyng.domain.enumeration.WindowBase;
import com.thyng.domain.enumeration.WindowType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Window implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull private WindowBase base;
	@NotNull private WindowType type;
	@Builder.Default @Min(60) private Long timeout = Duration.ofHours(1).getSeconds();
	@Builder.Default @Min(2) private Long span = Duration.ofHours(1).getSeconds();
	@Builder.Default @Min(1) private Long slidingSpan = Duration.ofMinutes(5).getSeconds();

}
