package com.rallies.networking.protocols.dto;

import java.io.Serial;
import java.io.Serializable;

public class ParticipantDto implements Serializable {
    private String teamName;
    private String engineCapacity;
    private String participantName;
    private String teamId;
    private String rallyId;

    public ParticipantDto(String teamName,
                          String engineCapacity,
                          String participantName,
                          String teamId,
                          String rallyId) {
        this.teamName = teamName;
        this.engineCapacity = engineCapacity;
        this.participantName = participantName;
        this.teamId = teamId;
        this.rallyId = rallyId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getEngineCapacity() {
        return engineCapacity;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getRallyId() {
        return rallyId;
    }
}
