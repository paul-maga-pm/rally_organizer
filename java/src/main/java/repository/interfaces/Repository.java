package repository.interfaces;

import domain.models.Identifiable;

import java.util.Collection;
import java.util.Optional;

public interface Repository <Id, E extends Identifiable<Id>> {
    Optional<E> save(E model);
    Optional<E> findById(Id id);
    Collection<E> getAll();
    boolean update(E model);
    Optional<E> delete(Id id);
}
