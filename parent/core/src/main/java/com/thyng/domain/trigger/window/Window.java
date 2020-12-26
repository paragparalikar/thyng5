package com.thyng.domain.trigger.window;

import java.io.Serializable;
import java.time.Duration;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@DynamoDBDocument
public class Window implements Serializable{
	private static final long serialVersionUID = 3378667085244182847L;

	@NotNull 
	private WindowBase base;
	
	@NotNull
	private WindowType type;
	
	@NotNull
	@Positive
	@Builder.Default
	private Long tumblingInterval = Duration.ofHours(1).getSeconds();
	
	@NotNull
	@Positive
	@Builder.Default
	private Long slidingInterval = Duration.ofMinutes(5).getSeconds();
	
}
