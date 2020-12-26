package com.thyng.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ManagedContext;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.spring.context.SpringManagedContext;

@Configuration
public class CacheConfiguration {
	
	@Bean
	public ManagedContext managedContext() {
		return new SpringManagedContext();
	}
	
	@Bean
	@Profile("jet-client")
	public JetInstance jetClientInstance(ManagedContext managedContext) {
		final ClientConfig clientConfig = new ClientConfig();
		clientConfig.setManagedContext(managedContext);
		return Jet.newJetClient(clientConfig);
	}
	
	@Bean
	@Profile("jet-member")
	public JetInstance jetMemberInstance(ManagedContext managedContext) {
		final Config config = new Config();
		config.setManagedContext(managedContext);
		final JetConfig jetConfig = new JetConfig();
		jetConfig.setHazelcastConfig(config);
		return Jet.newJetInstance(jetConfig);
	}

	@Bean
	public HazelcastInstance hazelcastInstance(JetInstance jetInstance) {
		return jetInstance.getHazelcastInstance();
	}

}
