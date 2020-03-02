package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Paginated<T> {

  private final String next;
  private List<T> results;
  private final String duration;

  public String getNext() {
    return next;
  }

  public List<T> getResults() {
    return results;
  }

  public void setResults(List<T> results) {
    this.results = results;
  }

  public String getDuration() {
    return duration;
  }

  @JsonCreator
  private Paginated(
      @JsonProperty("next")
          String next,
      @JsonProperty("results")
          List<T> results,
      @JsonProperty("duration")
          String duration
  ) {
    this.next = next;
    this.results = results;
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Paginated that = (Paginated) o;
    return next.equals(that.next) &&
        duration.equals(that.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), next, duration);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("next", this.next)
        .add("results", this.results)
        .add("duration", this.duration)
        .toString();
  }
}
