package com.thyng.domain.trigger;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.thyng.domain.trigger.window.Window;
import com.thyng.persistence.AbstractEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@DynamoDBTable(tableName = Trigger.TABLE_NAME)
public class Trigger extends AbstractEntity {
	private static final long serialVersionUID = 8755396643820803042L;
	public static final String TABLE_NAME = "trigger";

	@DynamoDBRangeKey private Integer id;
	@DynamoDBHashKey @NotNull @Positive private Integer tenantId;
	@NotNull private Boolean enabled;
	@NotNull private Language language;
	@NotNull private EventType eventType;
	@Valid private Window window;
	@NotBlank private String thingSelectionScript;
	@NotBlank private String evaluationScript;
	@NotBlank @Size(min = 3, max = 255) private String name;
	
}
