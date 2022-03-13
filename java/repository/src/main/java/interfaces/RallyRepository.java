package interfaces;

import models.Rally;

public interface RallyRepository extends Repository<Long, Rally> {
    Rally findByEngineCapacity(int engineCapacity);
}
