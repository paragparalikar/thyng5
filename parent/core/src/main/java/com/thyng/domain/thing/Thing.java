package com.thyng.domain.thing;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.thyng.persistence.AbstractEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@DynamoDBTable(tableName = Thing.TABLE_NAME)
public class Thing extends AbstractEntity {
	private static final long serialVersionUID = -2269905804622007163L;
	public static final String TABLE_NAME = "thing";

	@DynamoDBRangeKey private Integer id;
	@DynamoDBHashKey @NotNull @Positive private Integer tenantId;
	@DynamoDBTyped(DynamoDBAttributeType.M) private Map<String, String> attributes;
	@NotNull @Min(60) private Integer inactivityPeriod;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotNull @DynamoDBTypeConvertedEnum private ThingStatus status;
	
}
