package business.services;

import repository.interfaces.TeamRepository;
import models.Team;

public class TeamService {
    private TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team addTeam(String teamName) {
        Team team = new Team(teamName);
        return teamRepository.save(team);
    }

}
