package com.thyng.domain.model;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.thyng.domain.enumeration.ActionType;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@With
@Jacksonized
@SuperBuilder
@AllArgsConstructor
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.EXISTING_PROPERTY, 
  property = "actionType",
  visible = true)
@JsonSubTypes({ 
  @Type(value = MailAction.class, name = "MAIL")
})
public class Action implements TenantAwareModel<Action> {
	private static final long serialVersionUID = 1L;

	private final String id;
	private final String tenantId;
	private final Boolean enabled;
	private final ActionType actionType;
	private final String name;
	@Builder.Default private final Long rateLimit = Duration.ofHours(1).getSeconds();

}
