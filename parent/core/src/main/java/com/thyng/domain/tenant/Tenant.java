package com.thyng.domain.tenant;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@DynamoDBTable(tableName = Tenant.TABLE_NAME)
@EqualsAndHashCode(callSuper = false, of={"id", "name"})
public class Tenant implements Identifiable<Integer>, Nameable, Serializable {
	private static final long serialVersionUID = -4444782720017595800L;
	public static final String TABLE_NAME = "tenant";

	@DynamoDBHashKey private Integer id;
	@NotNull private Boolean enabled = Boolean.TRUE;
	@NotBlank @Size(min = 3, max = 255) private String name;
	
}
