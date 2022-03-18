package repository.interfaces;

import models.Rally;

import java.util.Optional;

public interface RallyRepository extends Repository<Long, Rally> {
    Optional<Rally> findRallyByEngineCapacity(int engineCapacity);
}
