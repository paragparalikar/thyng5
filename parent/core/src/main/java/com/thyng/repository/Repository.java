package com.thyng.repository;

import com.thyng.Crud;
import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Nameable;

public interface Repository<T extends Identifiable<ID> & Nameable, ID> extends Crud<T, ID>, Lifecycle {

}
