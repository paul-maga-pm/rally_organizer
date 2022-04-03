package com.rallies.domain.models;

import java.util.Objects;

public class Rally extends Identifiable<Long> {
    private int engineCapacity;
    private int numberOfParticipants = 0;

    public Rally(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public Rally(int engineCapacity, int numberOfParticipants) {
        this.engineCapacity = engineCapacity;
        this.numberOfParticipants = numberOfParticipants;
    }

    public Rally(Rally other) {
        this.engineCapacity = other.engineCapacity;
        this.numberOfParticipants = other.numberOfParticipants;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
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
    public String toString() {
        return "Rally{" +
                "engineCapacity=" + engineCapacity +
                ", numberOfParticipants=" + numberOfParticipants +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), engineCapacity);
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
}
