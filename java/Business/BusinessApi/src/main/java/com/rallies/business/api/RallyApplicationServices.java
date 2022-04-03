package com.rallies.business.api;

import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;

import java.util.Collection;
import java.util.Optional;

public interface RallyApplicationServices extends RalliesObservable {
    Participant  addParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName);
    Collection<Participant> getAllMembersOfTeam(String teamName);
    Optional<Participant> getParticipantByName(String participantName);

    Rally addRally(int engineCapacity);
    Collection<? extends Rally> getAllRallies();

    Team addTeam(String teamName);
    Collection<Team> getAllTeams();
    Optional<Team> getTeamByName(String teamName);

    void login(String username, String password);
    void logout();
}
