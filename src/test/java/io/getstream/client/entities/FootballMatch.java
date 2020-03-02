package io.getstream.client.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FootballMatch extends Match {
  private int nrOfPenalty;
  private int nrOfScore;

  public int getNrOfPenalty() {
    return nrOfPenalty;
  }

  public void setNrOfPenalty(int nrOfPenalty) {
    this.nrOfPenalty = nrOfPenalty;
  }

  public int getNrOfScore() {
    return nrOfScore;
  }

  public void setNrOfScore(int nrOfScore) {
    this.nrOfScore = nrOfScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    FootballMatch that = (FootballMatch) o;
    return nrOfPenalty == that.nrOfPenalty && nrOfScore == that.nrOfScore;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), nrOfPenalty, nrOfScore);
  }
}
