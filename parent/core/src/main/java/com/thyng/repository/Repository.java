package com.thyng.repository;

import com.thyng.domain.intf.Crud;
import com.thyng.domain.intf.Identifiable;

public interface Repository<T extends Identifiable<T>> extends Crud<T> {

}
