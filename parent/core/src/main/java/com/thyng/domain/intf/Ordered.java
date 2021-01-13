package com.thyng.domain.intf;

public interface Ordered extends Comparable<Ordered> {
	
	int getOrder();

	@Override
	default int compareTo(Ordered o) {
		return null == o ? 1 : (getOrder() - o.getOrder());
	}
	
}
