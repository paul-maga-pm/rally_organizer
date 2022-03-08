package models;

import java.util.Objects;

public class RallyParticipant extends Identifiable<Long> {
    private String firstName;
    private String lastName;
    private RallyTeam team;
    private Rally rally;

    public RallyParticipant(RallyTeam team, Rally rally, String firstName, String lastName) {
        this.team = team;
        this.rally = rally;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public RallyTeam getTeam() {
        return team;
    }

    public Rally getRally() {
        return rally;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RallyParticipant)) return false;
        if (!super.equals(o)) return false;
        RallyParticipant that = (RallyParticipant) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(team, that.team) &&
                Objects.equals(rally, that.rally);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, team, rally);
    }
}
