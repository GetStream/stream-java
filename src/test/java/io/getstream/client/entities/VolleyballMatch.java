package io.getstream.client.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Objects;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VolleyballMatch extends Match {
    private int nrOfServed;
    private int nrOfBlocked;

    public int getNrOfServed() {
        return nrOfServed;
    }

    public void setNrOfServed(int nrOfServed) {
        this.nrOfServed = nrOfServed;
    }

    public void setNrOfBlocked(int nrOfBlocked) {
        this.nrOfBlocked = nrOfBlocked;
    }

    public int getNrOfBlocked() {
        return nrOfBlocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VolleyballMatch that = (VolleyballMatch) o;
        return nrOfServed == that.nrOfServed &&
                nrOfBlocked == that.nrOfBlocked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nrOfServed, nrOfBlocked);
    }
}
