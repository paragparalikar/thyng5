package com.thyng.domain.model;

import java.io.Serializable;
import java.time.Duration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.thyng.domain.enumeration.WindowBase;
import com.thyng.domain.enumeration.WindowType;

import lombok.Data;

@Data
public class Window implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull private WindowBase base;
	@NotNull private WindowType type;
	@NotNull @Min(2) private Long tumblingInterval = Duration.ofHours(1).getSeconds();
	@NotNull @Min(1) private Long slidingInterval = Duration.ofMinutes(5).getSeconds();

}
