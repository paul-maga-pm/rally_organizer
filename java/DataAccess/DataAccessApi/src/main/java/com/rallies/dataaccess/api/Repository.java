package com.rallies.dataaccess.api;

import com.rallies.domain.models.Identifiable;

import java.util.Collection;

public interface Repository <ID, M extends Identifiable<ID>> {
    M save(M model);
    M findOne(ID modelID);
    Collection<M> findAll();

}
