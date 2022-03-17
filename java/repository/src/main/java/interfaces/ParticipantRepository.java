package interfaces;

import models.Participant;

import java.util.Optional;

public interface ParticipantRepository extends Repository<Long, Participant> {
    Iterable<Participant> findMembersOfTeam(String teamName);
    Optional<Participant> findParticipantByName(String participantName);
}
