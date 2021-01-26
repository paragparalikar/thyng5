package com.thyng.repository;

import com.thyng.domain.intf.Crud;
import com.thyng.domain.intf.Identifiable;
import com.thyng.domain.intf.Lifecycle;
import com.thyng.domain.intf.Nameable;

public interface Repository<T extends Identifiable<T, ID> & Nameable, ID> extends Crud<T, ID>, Lifecycle {

}
