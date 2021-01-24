package com.thyng.domain.model.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDto implements Serializable{
	private static final long serialVersionUID = -8228279260362197206L;
	
	@NotBlank private String thingId;
	@NotNull @Positive private Long timestamp;
	private final Map<@NotBlank String,@NotNull Object> values = new HashMap<>();

}
