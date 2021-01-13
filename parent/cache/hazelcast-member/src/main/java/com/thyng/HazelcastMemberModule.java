package com.thyng;

import com.hazelcast.jet.Jet;
import com.thyng.domain.intf.Module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class HazelcastMemberModule implements Module {
	
	@Setter private Context context;
	@Getter private final int order = 0;

	@Override
	public void start() throws Exception {
		context.setJetInstnace(Jet.newJetInstance());
		context.setHazelcastInstance(context.getJetInstnace().getHazelcastInstance());
	}
	
	@Override
	public void stop() throws Exception {
		context.getJetInstnace().shutdown();
	}
	
}
