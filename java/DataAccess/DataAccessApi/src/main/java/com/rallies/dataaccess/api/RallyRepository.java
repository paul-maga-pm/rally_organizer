package com.rallies.dataaccess.api;

import com.rallies.domain.models.Rally;

import java.util.Optional;

public interface RallyRepository extends Repository<Long, Rally> {
    Optional<Rally> findRallyByEngineCapacity(int engineCapacity);
}
