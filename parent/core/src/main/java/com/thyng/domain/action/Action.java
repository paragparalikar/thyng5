package com.thyng.domain.action;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.thyng.domain.tenant.TenantAwareEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@DynamoDBTable(tableName = Action.TABLE_NAME)
public class Action extends TenantAwareEntity{
	private static final long serialVersionUID = -2403149493576305238L;
	public static final String TABLE_NAME = "action";

	@DynamoDBRangeKey private String id;
	@DynamoDBHashKey @NotNull @Positive private String tenantId;
	@NotNull private Boolean enabled;
	@NotNull private ActionType actionType;
	@NotBlank @Size(min = 3, max = 255) private String name;
	
}
