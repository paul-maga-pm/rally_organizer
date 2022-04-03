package com.rallies.networking.protocols.dto;

import com.rallies.domain.models.Participant;
import com.rallies.domain.models.Rally;
import com.rallies.domain.models.Team;
import com.rallies.domain.models.User;

import java.util.Optional;

public class DtoUtils {
    public static User getModelFromDto(UserDto dto) {
        return new User(dto.getUsername(), dto.getPassword());
    }

    public static UserDto getDtoFromModel(User user) {
        return new UserDto(user.getUserName(), user.getPassword());
    }

    public static Participant[] getModelFromDto(ParticipantDto[] participantDtos) {
        Participant[] participants = new Participant[participantDtos.length];
        int i = 0;

        for(var partDto : participantDtos) {
            participants[i++] = getModelFromDto(partDto);
        }
        return participants;
    }

    public static ParticipantDto getDtoFromModel(Participant participant) {
        var teamName = participant.getTeam().getTeamName();
        var teamId = String.valueOf(participant.getTeam().getId());

        var engCap = String.valueOf(participant.getRally().getEngineCapacity());
        var noOfPart = String.valueOf(participant.getRally().getNumberOfParticipants());

        var rallyId = String.valueOf(participant.getRally().getId());

        var participantName = participant.getParticipantName();

        return new ParticipantDto(teamName, engCap, noOfPart ,participantName, teamId, rallyId);
    }

    public static Participant getModelFromDto(ParticipantDto dto) {
        var rallyId = Long.parseLong(dto.getRallyId());
        var noOfPart = Integer.parseInt(dto.getNumberOfParticipants());
        var rally = new Rally(Integer.parseInt(dto.getEngineCapacity()), noOfPart);
        var teamId = Long.parseLong(dto.getTeamId());
        var team = new Team(dto.getTeamName());

        team.setId(teamId);
        rally.setId(rallyId);
        return new Participant(team, rally, dto.getParticipantName());
    }


    public static ParticipantDto[] getDtoFromModel(Participant[] toArray) {
        var participantDto = new ParticipantDto[toArray.length];
        for(int i = 0; i < toArray.length; i++)
            participantDto[i] = getDtoFromModel(toArray[i]);
        return participantDto;
    }

    public static Team[] getModelFromDto(TeamDto[] teamDtos) {
        Team[] teams = new Team[teamDtos.length];
        int i = 0;

        for(var teamDto :teamDtos) {
            var id = Long.parseLong(teamDto.getTeamId());
            var name = teamDto.getTeamName();
            var team = new Team(name);
            team.setId(id);
            teams[i++] = team;
        }

        return teams;
    }

    public static TeamDto[] getDtoFromModel(Team[] teams) {
        TeamDto[] teamDtos = new TeamDto[teams.length];
        int i = 0;

        for(var team : teams) {
            teamDtos[i++] = new TeamDto(String.valueOf(team.getId()), team.getTeamName());
        }

        return teamDtos;
    }

    public static Rally[] getModelFromDto(RallyDto[] rallyDtos) {
        Rally[] rallies = new Rally[rallyDtos.length];

        for(int i = 0; i < rallies.length; i++) {
            var cap = Integer.parseInt(rallyDtos[i].getEngineCapacity());
            var noOfParticipants = Integer.valueOf(rallyDtos[i].getNumberOfParticipants());
            var id = Long.parseLong(rallyDtos[i].getRallyId());
            rallies[i] = new Rally(cap, noOfParticipants);
            rallies[i].setId(id);
        }

        return rallies;
    }

    public static RallyDto[] getDtoFromModel(Rally[] toArray) {
        RallyDto[] dtos = new RallyDto[toArray.length];

        for (int i = 0; i < toArray.length; i++) {
            String rallyId = String.valueOf(toArray[i].getId());
            String engineCapaicity = String.valueOf(toArray[i].getEngineCapacity());
            String numberOfParticipants = String.valueOf(toArray[i].getNumberOfParticipants());
            dtos[i] = new RallyDto(rallyId, engineCapaicity, numberOfParticipants);
        }

        return dtos;
    }

    public static ParticipantDto getDtoFromModel(Optional<Participant> participant) {
        ParticipantDto dto = null;

        if (participant.isPresent())
            dto = getDtoFromModel(participant.get());

        return dto;
    }

    public static TeamDto getDtoFromModel(Team team) {
        return new TeamDto(String.valueOf(team.getId()), team.getTeamName());
    }

    public static ParticipantDto getDtoFromModel(Team participantTeam, Rally rallyParticipatesTo, String participantName) {
        var teamName = participantTeam.getTeamName();
        var teamId = String.valueOf(participantTeam.getId());

        var engCap = String.valueOf(rallyParticipatesTo.getEngineCapacity());
        var noOfPart = String.valueOf(rallyParticipatesTo.getNumberOfParticipants());
        var rallyId = String.valueOf(rallyParticipatesTo.getId());

        return new ParticipantDto(teamName, engCap, noOfPart, participantName, teamId, rallyId);
    }

    public static RallyDto getDtoFromModel(Rally addedRally) {
        String id = String.valueOf(addedRally.getId());
        String cap = String.valueOf(addedRally.getEngineCapacity());
        String part = String.valueOf(addedRally.getNumberOfParticipants());
        return new RallyDto(id, cap, part);
    }

    public static Rally getModelFromDto(RallyDto data) {
        Long id = Long.parseLong(data.getRallyId());
        int partNo = Integer.parseInt(data.getNumberOfParticipants());
        int engineCap = Integer.parseInt(data.getEngineCapacity());

        Rally rally = new Rally(engineCap, partNo);
        rally.setId(id);
        return rally;
    }

    public static Team getModelFromDto(TeamDto data) {
        Long id = Long.parseLong(data.getTeamId());
        String name = data.getTeamName();
        Team team = new Team(name);
        team.setId(id);
        return team;
    }

}
