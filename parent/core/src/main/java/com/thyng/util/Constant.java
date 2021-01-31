package com.thyng.util;

public interface Constant {

	String COUNTER = "counters";
	String TENANT = "tenants";
	String GATEWAY = "gateways";
	String TEMPALTE = "templates";
	String SENSOR = "sensors";
	String ACTUATOR = "actuators";
	String ATTRIBUTE = "attributes";
	String THING = "things";
	String THING_INFO = "thing-infos";
	String THING_GROUP = "thing-groups";
	String THING_GROUP_MAPPING = "thing-group-mappings";
	String TRIGGER = "triggers";
	String TRIGGER_INFO = "trigger-infos";
	String ACTION = "actions";
	String USER = "users";
	String USER_GROUP = "user-groups";
	String USER_GROUP_MAPPING = "user-group-mappings";
	String METRIC = "metrics";
	String OBJECT = "objects";
	String TRIGGER_EVALUATION_INFO = "trigger-evaluation-info";
	
	String NAME_DELIMITER = "-";
	String TOPIC_CREATED = "created";
	String TOPIC_UPDATED = "updated";
	String TOPIC_DELETED = "deleted";
	
	static String createdTopic(String entityName) { return String.join(NAME_DELIMITER, entityName, TOPIC_CREATED);}
	static String updatedTopic(String entityName) { return String.join(NAME_DELIMITER, entityName, TOPIC_UPDATED);}
	static String deletedTopic(String entityName) { return String.join(NAME_DELIMITER, entityName, TOPIC_DELETED);}

	int ORDER_MODULE_CORE = Integer.MAX_VALUE;
	int ORDER_MODULE_DYNAMO = 0;
	int ORDER_MODULE_TIMESTREAM = 0;
	int ORDER_MODULE_CACHE_MAP = 1;
	
}
