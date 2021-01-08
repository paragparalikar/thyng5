package com.thyng.domain.thing;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.thyng.domain.Identifiable;

import lombok.Data;

@Data
@DynamoDBDocument
public class Sensor implements Identifiable<String>, Serializable{
	private static final long serialVersionUID = 5122567002461543932L;

	private String id;
	@NotBlank private String unit;
	@NotBlank @Size(min = 3, max = 255) private String name;
	
}
