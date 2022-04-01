package com.rallies.business.impl;

import com.rallies.dataaccess.api.ParticipantRepository;
import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;

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

    public Optional<Participant> getParticipantByName(String participantName) {
        return participantRepository.findParticipantByName(participantName);
    }
}
