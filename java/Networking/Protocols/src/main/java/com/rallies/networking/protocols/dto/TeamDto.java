package com.rallies.networking.protocols.dto;

import java.io.Serializable;

public class TeamDto implements Serializable {
    private String teamId;
    private String teamName;

    public TeamDto(String teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }
}
