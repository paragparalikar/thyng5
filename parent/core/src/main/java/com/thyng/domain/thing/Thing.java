package com.thyng.domain.thing;

import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
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
import com.thyng.domain.tenant.TenantAwareEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@DynamoDBTable(tableName = Thing.TABLE_NAME)
public class Thing extends TenantAwareEntity {
	private static final long serialVersionUID = -2269905804622007163L;
	public static final String TABLE_NAME = "thing";

	@DynamoDBRangeKey private Long id;
	@DynamoDBHashKey @NotNull @Positive private Long tenantId;
	
	@NotNull @Min(60) private Integer inactivityPeriod;
	@NotBlank @Size(min = 3, max = 255) private String name;
	@NotNull @DynamoDBTypeConvertedEnum private ThingStatus status;
	@DynamoDBTyped(DynamoDBAttributeType.M) private Map<@NotBlank String,@NotBlank String> attributes;
	
	private Set<@NotNull @Valid Sensor> sensors;
	private Set<@NotNull @Valid Actuator> actuators;
	
}
