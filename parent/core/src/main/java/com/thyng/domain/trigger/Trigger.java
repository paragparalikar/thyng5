package com.thyng.domain.trigger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
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

	@DynamoDBRangeKey 
	@DynamoDBAutoGeneratedKey 
	private String id;
	
	@DynamoDBHashKey 
	private String tenantId;
	
	@NotBlank private String name;
	@NotNull private Boolean enabled;
	@NotNull private Language language;
	@NotNull private EventType eventType;
	private Window window;
	
}
