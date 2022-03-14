package interfaces;

import models.Team;

public interface TeamRepository extends Repository<Long, Team> {
    Team findTeamByName(String teamName);
}
