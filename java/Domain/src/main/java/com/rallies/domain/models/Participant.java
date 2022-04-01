package com.rallies.domain.models;

import java.util.Objects;

public class Participant extends Identifiable<Long> {
    private String participantName;
    private Team team;
    private Rally rally;

    public Participant(Team team, Rally rally, String participantName) {
        this.team = team;
        this.rally = rally;
        this.participantName = participantName;
    }

    public Participant(Participant other) {
        this.team = other.getTeam();
        this.rally = other.getRally();
        this.participantName = other.participantName;
    }

    public String getParticipantName() {
        return participantName;
    }

    public Team getTeam() {
        return team;
    }

    public Rally getRally() {
        return rally;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        if (!super.equals(o)) return false;
        Participant that = (Participant) o;
        return Objects.equals(participantName, that.participantName) &&
                Objects.equals(team, that.team) &&
                Objects.equals(rally, that.rally);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "participantName='" + participantName + '\'' +
                ", team=" + team +
                ", rally=" + rally +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), participantName, team, rally);
    }
}
