package models;

import java.util.Objects;

public class RallyTeam extends Identifiable<Long> {
    private String teamName;

    public RallyTeam(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RallyTeam)) return false;
        if (!super.equals(o)) return false;
        RallyTeam rallyTeam = (RallyTeam) o;
        return Objects.equals(teamName, rallyTeam.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teamName);
    }
}
