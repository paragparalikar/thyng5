package com.thyng.module;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.thyng.Context;
import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Nameable;
import com.thyng.domain.intf.TenantAwareModel;
import com.thyng.domain.model.Mapping;
import com.thyng.dynamo.command.CreateTableCommand;
import com.thyng.dynamo.command.CreateTenantAwareTableCommand;
import com.thyng.dynamo.mapper.ActionMapper;
import com.thyng.dynamo.mapper.ActuatorMapper;
import com.thyng.dynamo.mapper.AttributeMapper;
import com.thyng.dynamo.mapper.GatewayMapper;
import com.thyng.dynamo.mapper.Mapper;
import com.thyng.dynamo.mapper.MappingMapper;
import com.thyng.dynamo.mapper.SensorMapper;
import com.thyng.dynamo.mapper.TemplateMapper;
import com.thyng.dynamo.mapper.TenantMapper;
import com.thyng.dynamo.mapper.ThingGroupMapper;
import com.thyng.dynamo.mapper.ThingInfoMapper;
import com.thyng.dynamo.mapper.ThingMapper;
import com.thyng.dynamo.mapper.ThingStatusChangeHistoryMapper;
import com.thyng.dynamo.mapper.ThingStatusChangeMapper;
import com.thyng.dynamo.mapper.TriggerEvaluationInfoMapper;
import com.thyng.dynamo.mapper.TriggerInfoMapper;
import com.thyng.dynamo.mapper.TriggerMapper;
import com.thyng.dynamo.mapper.UserGroupMapper;
import com.thyng.dynamo.mapper.UserMapper;
import com.thyng.dynamo.mapper.WindowMapper;
import com.thyng.dynamo.repository.DynamoCounterRepository;
import com.thyng.dynamo.repository.DynamoMappingRepository;
import com.thyng.dynamo.repository.DynamoNameableRepository;
import com.thyng.dynamo.repository.DynamoObjectRepository;
import com.thyng.dynamo.repository.DynamoRepository;
import com.thyng.dynamo.repository.DynamoTenantAwareRepository;
import com.thyng.repository.CounterRepository;
import com.thyng.repository.MappingRepository;
import com.thyng.repository.NameableRepository;
import com.thyng.repository.ObjectRepository;
import com.thyng.repository.Repository;
import com.thyng.repository.TenantAwareRepository;
import com.thyng.util.Constant;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoRepositoryModule implements Module {
	
	@Getter private final int order = Constant.ORDER_MODULE_DYNAMO;
	@NonNull @Setter private Context context;
	
	private DynamoDbAsyncClient client;

	@Override
	public void start() throws Exception {
		this.client = client();
		createTablesIfNotExist();
		createRepositories();
	}
	
	private void createTablesIfNotExist() {
		final List<String> tables = client.listTables().join().tableNames();
		
		Stream.of(Constant.COUNTER, Constant.TENANT, Constant.THING_GROUP_MAPPING, Constant.USER_GROUP_MAPPING,
				Constant.THING_INFO, Constant.TRIGGER_INFO, Constant.OBJECT)
			.filter(tableName -> !tables.contains(tableName))
			.map(CreateTableCommand::new)
			.forEach(command -> command.execute(client));
		
		Stream.of(Constant.ACTUATOR, Constant.GATEWAY, Constant.SENSOR, Constant.TEMPALTE, 
				Constant.THING, Constant.THING_GROUP, Constant.TRIGGER, Constant.ACTION, Constant.USER, Constant.USER_GROUP)
			.filter(tableName -> !tables.contains(tableName))
			.map(CreateTenantAwareTableCommand::new)
			.forEach(command -> command.execute(client));
	}
	
	private void createRepositories() {
		final String delimiter = ",";
		final WindowMapper windowMapper = new WindowMapper();
		final MappingMapper mappingMapper = new MappingMapper();
		final SensorMapper sensorMapper = new SensorMapper(delimiter);
		final ActuatorMapper actuatorMapper = new ActuatorMapper(delimiter);
		final AttributeMapper attributeMapper = new AttributeMapper(delimiter);
		final TemplateMapper templateMapper = new TemplateMapper(sensorMapper, actuatorMapper, attributeMapper);
		final CounterRepository counterRepository = counterRepository(client);
		context.setCounterRepository(counterRepository);
		
		context.setTenantRepository(nameableRepository(Constant.TENANT, new TenantMapper(), client, counterRepository));
		context.setGatewayRepository(tenantAwareRepository(Constant.GATEWAY, new GatewayMapper(), client, counterRepository));
		context.setTemplateRepository(tenantAwareRepository(Constant.TEMPALTE, templateMapper, client, counterRepository));
		context.setThingRepository(tenantAwareRepository(Constant.THING, new ThingMapper(attributeMapper), client, counterRepository));
		context.setThingInfoRepository(repository(Constant.THING_INFO, client, new ThingInfoMapper()));
		context.setThingGroupRepository(tenantAwareRepository(Constant.THING_GROUP, new ThingGroupMapper(), client, counterRepository));
		context.setThingGroupMappingRepository(mappingRepository(Constant.THING_GROUP_MAPPING, client, mappingMapper));
		context.setTriggerRepository(tenantAwareRepository(Constant.TRIGGER, new TriggerMapper(windowMapper), client, counterRepository));
		context.setTriggerInfoRepository(repository(Constant.TRIGGER_INFO, client, new TriggerInfoMapper()));
		context.setActionRepository(tenantAwareRepository(Constant.ACTION, new ActionMapper(), client, counterRepository));
		context.setUserRepository(tenantAwareRepository(Constant.USER, new UserMapper(attributeMapper), client, counterRepository));
		context.setUserGroupRepository(tenantAwareRepository(Constant.USER_GROUP, new UserGroupMapper(), client, counterRepository));
		context.setUserGroupMappingRepository(mappingRepository(Constant.USER_GROUP_MAPPING, client, mappingMapper));
		context.setTriggerEvaluationInfoRepository(objectRepository(Constant.OBJECT, client, new TriggerEvaluationInfoMapper()));
		context.setThingStatusChangeInfoRepository(objectRepository(Constant.OBJECT, client, new ThingStatusChangeHistoryMapper(new ThingStatusChangeMapper())));
	}
	
	private <T extends Identifiable<T>> ObjectRepository<T> objectRepository(String tableName,
			DynamoDbAsyncClient client, Mapper<T, Map<String, AttributeValue>> mapper){
		return DynamoObjectRepository.<T>builder()
				.client(client)
				.mapper(mapper)
				.tableName(tableName)
				.build();
	}
	
	private <T extends Identifiable<T>> Repository<T> repository(String tableName, 
			DynamoDbAsyncClient client, Mapper<T, Map<String, AttributeValue>> mapper){
		return DynamoRepository.<T>builder()
				.client(client)
				.mapper(mapper)
				.tableName(tableName)
				.build();
	}
	
	private CounterRepository counterRepository(DynamoDbAsyncClient client) {
		return DynamoCounterRepository.builder()
				.client(client)
				.build();
	}
	
	private MappingRepository mappingRepository(String tableName, DynamoDbAsyncClient client, Mapper<Mapping, Map<String, AttributeValue>> mapper) {
		return DynamoMappingRepository.builder()
				.client(client)
				.mapper(mapper)
				.tableName(tableName)
				.build();
	}
	
	private <T extends Identifiable<T> & Nameable> NameableRepository<T> nameableRepository(String tableName, 
			Mapper<T, Map<String, AttributeValue>> mapper, DynamoDbAsyncClient client, 
			CounterRepository counterRepository){
		return DynamoNameableRepository.<T>nameableRepositoryBuilder()
				.client(client)
				.mapper(mapper)
				.tableName(tableName)
				.counterRepository(counterRepository)
				.build();
	}
	
	private <T extends TenantAwareModel<T>> TenantAwareRepository<T> tenantAwareRepository(String tableName,
			Mapper<T, Map<String, AttributeValue>> mapper, DynamoDbAsyncClient client, 
			CounterRepository counterRepository) {
		return DynamoTenantAwareRepository.<T>builder()
				.client(client)
				.mapper(mapper)
				.tableName(tableName)
				.counterRepository(counterRepository)
				.build();
	}
	
	private DynamoDbAsyncClient client() {
		return DynamoDbAsyncClient.builder()
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
