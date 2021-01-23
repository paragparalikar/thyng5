package com.thyng;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import com.thyng.domain.intf.Module;
import com.thyng.dynamo.command.CreateTableCommand;
import com.thyng.dynamo.command.CreateTenantAwareTableCommand;
import com.thyng.dynamo.mapper.ActionMapper;
import com.thyng.dynamo.mapper.ActuatorMapper;
import com.thyng.dynamo.mapper.AttributeMapper;
import com.thyng.dynamo.mapper.GatewayMapper;
import com.thyng.dynamo.mapper.SensorMapper;
import com.thyng.dynamo.mapper.TemplateMapper;
import com.thyng.dynamo.mapper.TenantMapper;
import com.thyng.dynamo.mapper.ThingGroupMapper;
import com.thyng.dynamo.mapper.ThingMapper;
import com.thyng.dynamo.mapper.TriggerMapper;
import com.thyng.dynamo.mapper.UserGroupMapper;
import com.thyng.dynamo.mapper.UserMapper;
import com.thyng.dynamo.mapper.WindowMapper;
import com.thyng.dynamo.repository.DynamoCounterRepository;
import com.thyng.dynamo.repository.DynamoRepository;
import com.thyng.dynamo.repository.DynamoTemplateRepository;
import com.thyng.dynamo.repository.DynamoTenantAwareRepository;
import com.thyng.repository.CounterRepository;
import com.thyng.util.Names;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoRepositoryModule implements Module {
	
	@Getter private final int order = 0;
	@NonNull @Setter private Context context;
	@NonNull private DynamoDbClient client;

	@Override
	public void start() throws Exception {
		this.client = client();
		createTablesIfNotExist();
		createRepositories();
	}
	
	private void createTablesIfNotExist() {
		final List<String> tables = client.listTables().tableNames();
		if(!tables.contains(Names.COUNTER)) new CreateTableCommand(Names.COUNTER).execute(client);
		if(!tables.contains(Names.TENANT)) new CreateTableCommand(Names.TENANT).execute(client);
		Stream.of(Names.ACTUATOR, Names.GATEWAY, Names.SENSOR, Names.TEMPALTE, 
				Names.THING, Names.THING_GROUP, Names.TRIGGER, Names.ACTION, Names.USER, Names.USER_GROUP)
			.filter(tableName -> !tables.contains(tableName))
			.map(CreateTenantAwareTableCommand::new)
			.forEach(command -> command.execute(client));
	}
	
	private void createRepositories() {
		final String delimiter = ",";
		final WindowMapper windowMapper = new WindowMapper();
		final SensorMapper sensorMapper = new SensorMapper(delimiter);
		final ActuatorMapper actuatorMapper = new ActuatorMapper(delimiter);
		final AttributeMapper attributeMapper = new AttributeMapper(delimiter);
		final TemplateMapper templateMapper = new TemplateMapper(sensorMapper, actuatorMapper, attributeMapper);
		final CounterRepository counterRepository = new DynamoCounterRepository(client);
		context.setCounterRepository(counterRepository);
		context.setTenantRepository(new DynamoRepository<>(Names.TENANT, new TenantMapper(), client, counterRepository));
		context.setGatewayRepository(new DynamoTenantAwareRepository<>(Names.GATEWAY, new GatewayMapper(), client, counterRepository));
		context.setTemplateRepository(new DynamoTemplateRepository(templateMapper, client, counterRepository));
		context.setThingRepository(new DynamoTenantAwareRepository<>(Names.THING, new ThingMapper(attributeMapper), client, counterRepository));
		context.setThingGroupRepository(new DynamoTenantAwareRepository<>(Names.THING_GROUP, new ThingGroupMapper(), client, counterRepository));
		context.setTriggerRepository(new DynamoTenantAwareRepository<>(Names.TRIGGER, new TriggerMapper(windowMapper), client, counterRepository));
		context.setActionRepository(new DynamoTenantAwareRepository<>(Names.ACTION, new ActionMapper(), client, counterRepository));
		context.setUserRepository(new DynamoTenantAwareRepository<>(Names.USER, new UserMapper(attributeMapper), client, counterRepository));
		context.setUserGroupRepository(new DynamoTenantAwareRepository<>(Names.USER_GROUP, new UserGroupMapper(), client, counterRepository));
	}
	
	private DynamoDbClient client() {
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
		client.close();
	}

}
