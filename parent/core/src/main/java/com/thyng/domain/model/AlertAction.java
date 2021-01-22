package com.thyng.domain.model;

import java.time.Duration;

import javax.validation.constraints.NotNull;

import com.thyng.domain.enumeration.AlertType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlertAction extends Action {	
	private static final long serialVersionUID = 1L;

	@NotNull private AlertType alertType;
	@Builder.Default @NotNull private Integer rateLimit = (int) Duration.ofHours(1).getSeconds();
	
}
