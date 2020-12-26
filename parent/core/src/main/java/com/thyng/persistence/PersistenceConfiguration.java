package com.thyng.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;

@Configuration
public class PersistenceConfiguration {

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("accessKey", "secretKey")))
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
				.build();
	}

	@Bean
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
		return new DynamoDBMapper(amazonDynamoDB, ConsistentReads.CONSISTENT.config());
	}
	
}
