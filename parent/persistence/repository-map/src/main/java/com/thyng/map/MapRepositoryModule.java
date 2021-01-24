package com.thyng.map;

import com.thyng.Context;
import com.thyng.domain.intf.Module;

import lombok.Getter;
import lombok.Setter;

public class MapRepositoryModule implements Module {

	@Getter private int order = 0;
	@Setter private Context context;
	
	@Override
	public void start() throws Exception {
		context.setMetricRepository(new MapMetricRepository());
	}

}
