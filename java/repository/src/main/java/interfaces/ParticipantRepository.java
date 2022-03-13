package interfaces;

import models.Participant;

public interface ParticipantRepository extends Repository<Long, Participant> {
    Iterable<Participant> findByTeamName(String teamName);
    Participant findByParticipantName(String participantName);
}
