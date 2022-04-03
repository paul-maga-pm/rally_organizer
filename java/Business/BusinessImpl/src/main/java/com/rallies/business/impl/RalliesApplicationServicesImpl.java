package com.rallies.business.impl;

import com.rallies.business.api.RalliesObserver;
import com.rallies.business.api.RallyApplicationServices;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RalliesApplicationServicesImpl implements RallyApplicationServices {
    private ParticipantService participantService;
    private UserService userService;
    private RallyService rallyService;
    private TeamService teamService;

    private List<RalliesObserver> observers;

    public RalliesApplicationServicesImpl(ParticipantService participantService,
                                          UserService userService,
                                          RallyService rallyService,
                                          TeamService teamService) {
        this.participantService = participantService;
        this.userService = userService;
        this.rallyService = rallyService;
        this.teamService = teamService;

        observers = new ArrayList<>();
    }

    @Override
    public synchronized Participant addParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName) {
        Participant addedParticipant = participantService.addParticipant(participantTeam, rallyParticipatesTo, participantName);
        notifyObserversThatParticipantWasAdded(addedParticipant);
        return addedParticipant;
    }

    @Override
    public synchronized Collection<Participant> getAllMembersOfTeam(String teamName) {
        return participantService.getAllMembersOfTeam(teamName);
    }

    @Override
    public synchronized Optional<Participant> getParticipantByName(String participantName) {
        return participantService.getParticipantByName(participantName);
    }

    @Override
    public synchronized Rally addRally(int engineCapacity) {
        Rally addedRally = rallyService.addRally(engineCapacity);
        notifyObserversThatRallyWasAdded(addedRally);
        return addedRally;
    }

    @Override
    public synchronized Collection<? extends Rally> getAllRallies() {
        return rallyService.getAllRallies();
    }

    @Override
    public synchronized Team addTeam(String teamName) {
        Team addedTeam = teamService.addTeam(teamName);
        notifyObserversThatTeamWasAdded(addedTeam);
        return addedTeam;
    }

    @Override
    public synchronized Collection<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @Override
    public synchronized Optional<Team> getTeamByName(String teamName) {
        return teamService.getTeamByName(teamName);
    }

    @Override
    public synchronized void login(String username, String password) {
        userService.login(username, password);
    }

    @Override
    public synchronized void logout() {

    }

    @Override
    public synchronized void addObserver(RalliesObserver ralliesObserver) {
        observers.add(ralliesObserver);
    }

    @Override
    public synchronized void removeObserver(RalliesObserver ralliesObserver) {
        observers.remove(ralliesObserver);
    }

    @Override
    public synchronized void notifyObserversThatParticipantWasAdded(Participant addedParticipant) {
        for(var observer : observers)
            observer.updateParticipantWasAdded(addedParticipant);
    }

    @Override
    public synchronized void notifyObserversThatRallyWasAdded(Rally addedRally) {
        for(var observer : observers)
            observer.updateRallyWasAdded(addedRally);
    }

    @Override
    public synchronized void notifyObserversThatTeamWasAdded(Team addedTeam) {
        for(var observer : observers)
            observer.updateTeamWasAdded(addedTeam);
    }

    public void registerUser(String user, String password) {
        userService.register(user, password);
    }
}
