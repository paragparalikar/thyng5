package com.thyng.domain.actuator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.thyng.persistence.AbstractEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@DynamoDBTable(tableName = Actuator.TABLE_NAME)
public class Actuator extends AbstractEntity{
	private static final long serialVersionUID = -7972853698613197304L;
	public static final String TABLE_NAME = "actuator";

	@DynamoDBRangeKey private Integer id;
	@DynamoDBHashKey @NotNull @Positive private Integer tenantId;
	@NotBlank @Positive private Integer thingId;
	@NotBlank @Size(min = 3, max = 255) private String name;
	
	
}
