package com.thyng.domain.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
	
	private String thingId;
	private Long timestamp;
	private final Map<String,Object> values = new HashMap<>();

}
