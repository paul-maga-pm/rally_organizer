package models;

import java.util.Objects;

public class Identifiable<Id> {
    private Id id;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identifiable)) return false;
        Identifiable<?> identifiable = (Identifiable<?>) o;
        return Objects.equals(id, identifiable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
