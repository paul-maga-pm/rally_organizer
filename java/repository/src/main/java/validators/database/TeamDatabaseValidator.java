package validators.database;

import models.Team;
import validators.interfaces.TeamValidator;
import validators.interfaces.Validator;

public class TeamDatabaseValidator implements TeamValidator {
    @Override
    public void validate(Team model) {
        if (model.getTeamName().equals(""))
            throw new Validator.InvalidModelException("Team name can't be empty!");
    }
}
