package com.thyng.domain.tenant;

import java.io.Serializable;

import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;

public abstract class TenantAwareEntity implements Identifiable<String>, Nameable, TenantAware, Serializable {
	private static final long serialVersionUID = 8219489535294517385L;

}
