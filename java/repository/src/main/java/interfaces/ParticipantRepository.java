package interfaces;

import models.Participant;

public interface ParticipantRepository extends Repository<Long, Participant> {
    Iterable<Participant> findMembersOfTeam(String teamName);
    Participant findParticipantByName(String participantName);
}
