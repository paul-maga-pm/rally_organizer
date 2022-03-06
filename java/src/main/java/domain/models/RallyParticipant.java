package domain.models;

import java.util.Objects;

public class RallyParticipant extends Identifiable<Long> {
    private String firstName;
    private String lastName;
    private Long teamId;
    private Long rallyId;

    public RallyParticipant(Long teamId, Long rallyId, String firstName, String lastName) {
        this.teamId = teamId;
        this.rallyId = rallyId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getRallyId() {
        return rallyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RallyParticipant)) return false;
        if (!super.equals(o)) return false;
        RallyParticipant that = (RallyParticipant) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(rallyId, that.rallyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, teamId, rallyId);
    }
}
