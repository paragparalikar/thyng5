package com.thyng.domain.model;

import java.time.Duration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.thyng.domain.enumeration.ActionType;
import com.thyng.domain.intf.TenantAwareModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.EXISTING_PROPERTY, 
  property = "actionType",
  visible = true)
@JsonSubTypes({ 
  @Type(value = MailAction.class, name = "MAIL")
})
public class Action implements TenantAwareModel {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotBlank private String tenantId;
	@NotNull private Boolean enabled;
	@NotNull private ActionType actionType;
	@NotBlank @Size(max = 255) private String name;
	@Builder.Default @NotNull private Long rateLimit = Duration.ofHours(1).getSeconds();

}
