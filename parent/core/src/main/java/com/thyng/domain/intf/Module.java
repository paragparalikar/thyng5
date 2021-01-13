package com.thyng.domain.intf;

import com.thyng.Context;

public interface Module extends Lifecycle, Ordered {
	
	void setContext(Context context);

}
