package com.thyng.domain.sensor;

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
@DynamoDBTable(tableName = Sensor.TABLE_NAME)
public class Sensor extends AbstractEntity {
	private static final long serialVersionUID = 5122567002461543932L;
	public static final String TABLE_NAME = "sensor";

	@DynamoDBRangeKey private Integer id;
	@DynamoDBHashKey @NotNull @Positive private Integer tenantId;
	@NotBlank @Positive private Integer thingId;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotBlank private String unit;
}
