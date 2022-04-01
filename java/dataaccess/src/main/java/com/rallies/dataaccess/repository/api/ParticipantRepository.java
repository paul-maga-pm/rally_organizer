package com.rallies.dataaccess.repository.api;

import models.Participant;

import java.util.Collection;
import java.util.Optional;

public interface ParticipantRepository extends Repository<Long, Participant> {
    Collection<Participant> findMembersOfTeam(String teamName);
    Optional<Participant> findParticipantByName(String participantName);
}
