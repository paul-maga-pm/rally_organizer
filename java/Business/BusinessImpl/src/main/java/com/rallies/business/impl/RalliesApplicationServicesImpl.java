package com.rallies.business.impl;

import com.rallies.business.api.RallyApplicationServices;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;

import java.util.Collection;
import java.util.Optional;

public class RalliesApplicationServicesImpl implements RallyApplicationServices {
    private ParticipantService participantService;
    private UserService userService;
    private RallyService rallyService;
    private TeamService teamService;

    public RalliesApplicationServicesImpl(ParticipantService participantService,
                                          UserService userService,
                                          RallyService rallyService,
                                          TeamService teamService) {
        this.participantService = participantService;
        this.userService = userService;
        this.rallyService = rallyService;
        this.teamService = teamService;
    }

    @Override
    public Participant addParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName) {
        return participantService.addParticipant(participantTeam, rallyParticipatesTo, participantName);
    }

    @Override
    public Collection<Participant> getAllMembersOfTeam(String teamName) {
        return participantService.getAllMembersOfTeam(teamName);
    }

    @Override
    public Optional<Participant> getParticipantByName(String participantName) {
        return participantService.getParticipantByName(participantName);
    }

    @Override
    public Rally addRally(int engineCapacity) {
        return rallyService.addRally(engineCapacity);
    }

    @Override
    public Collection<? extends Rally> getAllRallies() {
        return rallyService.getAllRallies();
    }

    @Override
    public Team addTeam(String teamName) {
        return teamService.addTeam(teamName);
    }

    @Override
    public Collection<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @Override
    public Optional<Team> getTeamByName(String teamName) {
        return teamService.getTeamByName(teamName);
    }

    @Override
    public void login(String username, String password) {
        userService.login(username, password);
    }
}
