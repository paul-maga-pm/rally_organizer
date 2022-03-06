package models;

import java.util.Objects;

public class Rally extends Identifiable<Long> {
    private int engineCapacity;

    public Rally(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rally)) return false;
        if (!super.equals(o)) return false;
        Rally rally = (Rally) o;
        return engineCapacity == rally.engineCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), engineCapacity);
    }
}
