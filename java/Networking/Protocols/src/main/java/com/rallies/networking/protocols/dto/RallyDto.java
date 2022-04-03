package com.rallies.networking.protocols.dto;

import java.io.Serializable;

public class RallyDto implements Serializable {
    private String rallyId;
    private String engineCapacity;
    private String numberOfParticipants;

    public RallyDto(String rallyId, String engineCapacity, String numberOfParticipants) {
        this.rallyId = rallyId;
        this.engineCapacity = engineCapacity;
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getRallyId() {
        return rallyId;
    }

    public String getEngineCapacity() {
        return engineCapacity;
    }

    public String getNumberOfParticipants() {
        return numberOfParticipants;
    }
}
