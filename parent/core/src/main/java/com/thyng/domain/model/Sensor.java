package com.thyng.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.thyng.domain.intf.TemplateAwareModel;

import lombok.Data;

@Data
public class Sensor implements TemplateAwareModel {
	private static final long serialVersionUID = 1L;
	
	private String id;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotBlank @Size(min = 1, max = 255) private String unit;
	@NotBlank private String tenantId;
	@NotBlank private String templateId;

}
