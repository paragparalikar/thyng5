package com.thyng.domain.intf;

import java.io.Serializable;

public interface TenantAwareModel<T extends TenantAwareModel<T>> 
		extends Identifiable<T, String>, Nameable, TenantAware<T>, Serializable {

}
