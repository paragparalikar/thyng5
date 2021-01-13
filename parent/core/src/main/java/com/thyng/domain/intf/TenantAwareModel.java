package com.thyng.domain.intf;

import java.io.Serializable;

public interface TenantAwareModel extends Identifiable<String>, Nameable, TenantAware, Serializable {

}
