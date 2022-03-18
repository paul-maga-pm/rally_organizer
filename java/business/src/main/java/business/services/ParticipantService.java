package business.services;

import repository.interfaces.ParticipantRepository;
import models.Participant;
import models.Rally;
import models.Team;

import java.util.Collection;
import java.util.Optional;

public class ParticipantService {
    private ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant addParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName) {
        Participant participant = new Participant(participantTeam, rallyParticipatesTo, participantName);
        return participantRepository.save(participant);
    }

    public Collection<Participant> getAllMembersOfTeam(String teamName) {
        return participantRepository.findMembersOfTeam(teamName);
    }

    public Optional<Participant> findParticipantByName(String participantName) {
        return participantRepository.findParticipantByName(participantName);
    }
}
