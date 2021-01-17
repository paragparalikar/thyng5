package com.thyng.domain.model;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.TemplateAwareModel;

import lombok.Data;

@Data
public class Thing implements TemplateAwareModel {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotBlank private String tenantId;
	@NotBlank private String templateId;
	@NotNull @Min(60) private Integer inactivityPeriod = 60;
	private Map<@NotBlank String,@NotBlank String> attributes;

}
