package interfaces;

import exceptions.ExceptionBaseClass;
import models.Identifiable;

public interface Repository <ID, M extends Identifiable<ID>> {
    M save(M model);
    M findOne(ID modelID);
    Iterable<M> findAll();

}
