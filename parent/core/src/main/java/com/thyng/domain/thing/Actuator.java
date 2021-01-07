package com.thyng.domain.thing;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Data;

@Data
@DynamoDBDocument
public class Actuator implements Serializable {
	private static final long serialVersionUID = -7972853698613197304L;

	private String id;
	@NotBlank @Size(min = 3, max = 255) private String name;
	
}
