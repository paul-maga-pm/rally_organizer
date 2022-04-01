package com.rallies.business.services.impl;

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

    public Collection<Team> getAll() {
        return teamRepository.findAll();
    }

    public Optional<Team> findTeamByName(String teamName) {
        return teamRepository.findTeamByName(teamName);
    }
}
