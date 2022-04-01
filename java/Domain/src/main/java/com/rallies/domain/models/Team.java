package com.rallies.domain.models;

import java.util.Objects;

public class Team extends Identifiable<Long> {
    private String teamName;

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public Team(Team other) {
        this.teamName = other.teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        if (!super.equals(o)) return false;
        Team team = (Team) o;
        return Objects.equals(teamName, team.teamName);
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamName='" + teamName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teamName);
    }
}
