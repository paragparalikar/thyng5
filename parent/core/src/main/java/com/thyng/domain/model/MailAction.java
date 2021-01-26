package com.thyng.domain.model;

import java.util.Collections;
import java.util.Set;

import com.thyng.domain.enumeration.ActionType;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MailAction extends Action {
	private static final long serialVersionUID = 1L;

	private final String subject;
	private final String content;
	@Builder.Default private final Set<String> userGroupIds = Collections.emptySet();
	
	@Override
	public ActionType getActionType() {
		return ActionType.MAIL;
	}

}
