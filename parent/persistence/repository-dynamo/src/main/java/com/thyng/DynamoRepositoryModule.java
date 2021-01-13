package com.thyng;

import java.net.URI;
import java.util.List;

import com.thyng.domain.intf.Module;
import com.thyng.domain.model.Actuator;
import com.thyng.domain.model.Gateway;
import com.thyng.domain.model.Sensor;
import com.thyng.domain.model.Template;
import com.thyng.domain.model.Tenant;
import com.thyng.domain.model.Thing;
import com.thyng.domain.model.Trigger;
import com.thyng.dynamo.command.CreateTableCommand;
import com.thyng.dynamo.command.CreateTenantAwareTableCommand;
import com.thyng.dynamo.repository.ActuatorDynamoRepository;
import com.thyng.dynamo.repository.GatewayDynamoRepository;
import com.thyng.dynamo.repository.SensorDynamoRepository;
import com.thyng.dynamo.repository.TemplateDynamoRepository;
import com.thyng.dynamo.repository.TenantDynamoRepository;
import com.thyng.dynamo.repository.ThingDynamoRepository;
import com.thyng.dynamo.repository.TriggerDynamoRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@RequiredArgsConstructor
public class DynamoRepositoryModule implements Module {
	
	@Getter private final int order = 0;
	@Setter private Context context;
	private DynamoDbClient syncClient;
	private DynamoDbAsyncClient asyncClient;

	@Override
	public void start() throws Exception {
		this.syncClient = syncClient();
		this.asyncClient = asyncClient();
		createTablesIfNotExist();
		createRepositories();
	}
	
	private void createTablesIfNotExist() {
		final List<String> tables = syncClient.listTables().tableNames();
		if(!tables.contains(Tenant.CACHE_NAME)) new CreateTableCommand(Tenant.CACHE_NAME).execute(syncClient);
		if(!tables.contains(Actuator.CACHE_NAME)) new CreateTenantAwareTableCommand(Actuator.CACHE_NAME).execute(syncClient);
		if(!tables.contains(Gateway.CACHE_NAME)) new CreateTenantAwareTableCommand(Gateway.CACHE_NAME).execute(syncClient);
		if(!tables.contains(Sensor.CACHE_NAME)) new CreateTenantAwareTableCommand(Sensor.CACHE_NAME).execute(syncClient);
		if(!tables.contains(Template.CACHE_NAME)) new CreateTenantAwareTableCommand(Template.CACHE_NAME).execute(syncClient);
		if(!tables.contains(Thing.CACHE_NAME)) new CreateTenantAwareTableCommand(Thing.CACHE_NAME).execute(syncClient);
		if(!tables.contains(Trigger.CACHE_NAME)) new CreateTenantAwareTableCommand(Trigger.CACHE_NAME).execute(syncClient);
	}
	
	private void createRepositories() {
		context.setActuatorRepository(new ActuatorDynamoRepository(asyncClient));
		context.setGatewayRepository(new GatewayDynamoRepository(asyncClient));
		context.setSensorRepository(new SensorDynamoRepository(asyncClient));
		context.setTemplateRepository(new TemplateDynamoRepository(asyncClient));
		context.setTenantRepository(new TenantDynamoRepository(asyncClient));
		context.setThingRepository(new ThingDynamoRepository(asyncClient));
		context.setTriggerRepository(new TriggerDynamoRepository(asyncClient));
	}
	
	private DynamoDbAsyncClient asyncClient() {
		return DynamoDbAsyncClient.builder()
				.region(Region.AP_SOUTH_1)
				.credentialsProvider(credentialsProvider())
				.endpointOverride(URI.create("http://localhost:8000"))
				.build();
	}
	
	private DynamoDbClient syncClient() {
		return DynamoDbClient.builder()
				.region(Region.AP_SOUTH_1)
				.credentialsProvider(credentialsProvider())
				.endpointOverride(URI.create("http://localhost:8000"))
				.build();
	}

	private AwsCredentialsProvider credentialsProvider() {
		final AwsCredentials credentials = AwsBasicCredentials.create("test", "test");
		return StaticCredentialsProvider.create(credentials);
	}
	
	@Override
	public void stop() throws Exception {
		syncClient.close();
		asyncClient.close();
	}

}
