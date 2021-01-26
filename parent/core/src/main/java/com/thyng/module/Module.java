package com.thyng.module;

import com.thyng.Context;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Ordered;

public interface Module extends Lifecycle, Ordered {
	
	void setContext(Context context);

}
