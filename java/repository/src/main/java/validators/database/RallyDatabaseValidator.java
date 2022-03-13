package validators.database;

import models.Rally;
import validators.interfaces.RallyValidator;

public class RallyDatabaseValidator implements RallyValidator {
    @Override
    public void validate(Rally rally) {
        if (rally.getEngineCapacity() <= 0)
            throw new InvalidModelException("Engine capacity can't be negative!");
    }
}
