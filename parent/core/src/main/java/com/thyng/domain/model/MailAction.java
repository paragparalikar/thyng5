package com.thyng.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.thyng.domain.enumeration.ActionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MailAction extends Action {
	private static final long serialVersionUID = 1L;

	@NotBlank private String subject;
	@NotBlank private String content;
	private final Set<@NotBlank String> userGroupIds = new HashSet<>();
	
	@Override
	public ActionType getActionType() {
		return ActionType.MAIL;
	}
	
	@Override
	public void setActionType(ActionType actionType) {
		if(!ActionType.MAIL.equals(actionType)) {
			throw new IllegalArgumentException();
		}
		super.setActionType(ActionType.MAIL);
	}

}
