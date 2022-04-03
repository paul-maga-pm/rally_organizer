package com.rallies.business.api;

import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;

public interface RalliesObservable {
    void addObserver(RalliesObserver ralliesObserver);
    void removeObserver(RalliesObserver ralliesObserver);
    void notifyObserversThatParticipantWasAdded(Participant addedParticipant);
    void notifyObserversThatRallyWasAdded(Rally addedRally);
    void notifyObserversThatTeamWasAdded(Team addedTeam);
}
