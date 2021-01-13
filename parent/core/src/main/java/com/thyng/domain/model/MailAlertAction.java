package com.thyng.domain.model;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MailAlertAction extends AlertAction {
	private static final long serialVersionUID = 1L;

	@NotNull @NotEmpty private Set<@Email String> to;
	@NotBlank private String subject;
	@NotBlank private String content;

}
