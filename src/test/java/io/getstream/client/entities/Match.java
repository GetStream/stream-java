package io.getstream.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = VolleyballMatch.class, name = "volley"),
  @JsonSubTypes.Type(value = FootballMatch.class, name = "football")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
  public String actor;
  public String verb;
  public String object;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Match match = (Match) o;
    return Objects.equals(actor, match.actor)
        && Objects.equals(verb, match.verb)
        && Objects.equals(object, match.object);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actor, verb, object);
  }
}
