package com.thyng.persistence;

import java.io.Serializable;

import com.thyng.domain.Identifiable;
import com.thyng.domain.Nameable;
import com.thyng.domain.tenant.TenantAware;

public abstract class AbstractEntity implements Identifiable<String>, Nameable, TenantAware, Serializable {
	private static final long serialVersionUID = 8219489535294517385L;

}
