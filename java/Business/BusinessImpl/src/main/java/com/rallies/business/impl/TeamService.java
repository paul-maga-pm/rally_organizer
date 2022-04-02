package com.rallies.business.impl;

import com.rallies.dataaccess.api.TeamRepository;
import com.rallies.domain.models.Team;

import java.util.Collection;
import java.util.Optional;

public class TeamService {
    private TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team addTeam(String teamName) {
        Team team = new Team(teamName);
        return teamRepository.save(team);
    }

    public Collection<Team> getAllTeams() {
        return teamRepository.getAll();
    }

    public Optional<Team> getTeamByName(String teamName) {
        return teamRepository.getTeamByName(teamName);
    }
}
