package com.rallies.business.api;

import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;

public interface RalliesObserver {
    void updateTeamWasAdded(Team addedTeam);
    void updateParticipantWasAdded(Participant addedParticipant);
    void updateRallyWasAdded(Rally addedRally);
}
