package com.rallies.dataaccess.api;

import com.rallies.domain.models.Participant;

import java.util.Collection;
import java.util.Optional;

public interface ParticipantRepository extends Repository<Long, Participant> {
    Collection<Participant> getMembersOfTeam(String teamName);
    Optional<Participant> getParticipantByName(String participantName);
}
