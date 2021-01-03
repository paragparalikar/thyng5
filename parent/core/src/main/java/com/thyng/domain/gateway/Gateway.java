package com.thyng.domain.gateway;

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
@DynamoDBTable(tableName = Gateway.TABLE_NAME)
public class Gateway extends AbstractEntity {
	private static final long serialVersionUID = 6186495974401788683L;
	public static final String TABLE_NAME = "gateway";

	@DynamoDBRangeKey private Integer id;
	@DynamoDBHashKey @NotNull @Positive private Integer tenantId;
	@NotBlank @Size(min = 3, max = 255) private String name;
	private String publicKey;
	private String privateKey;
	
}
