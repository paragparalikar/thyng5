package com.thyng.domain.action.alert;

import java.time.Duration;

import javax.validation.constraints.NotNull;

import com.thyng.domain.action.Action;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlertAction extends Action {
	private static final long serialVersionUID = 101858195867294486L;

	@NotNull 
	private Integer rateLimit = (int) Duration.ofHours(1).getSeconds();
	
	@NotNull 
	private AlertType alertType;
	
}
