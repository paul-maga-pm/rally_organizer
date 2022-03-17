package service;

import interfaces.ParticipantRepository;
import models.Participant;
import models.Rally;
import models.Team;

public class ParticipantService {
    private ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant addParticipant(Team participantTeam, Rally rallyParticipatesTo, String participantName) {
        Participant participant = new Participant(participantTeam, rallyParticipatesTo, participantName);
        return participantRepository.save(participant);
    }

    public Iterable<Participant> getAllMembersOfTeam(String teamName) {
        return participantRepository.findMembersOfTeam(teamName);
    }
}
