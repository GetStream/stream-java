package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.util.Objects;

public final class Feature {
  private final String group;
  private final String value;

  @JsonCreator
  public Feature(@JsonProperty("group") String group, @JsonProperty("value") String value) {
    this.group = group;
    this.value = value;
  }

  public String getGroup() {
    return group;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Feature feature = (Feature) o;
    return Objects.equals(group, feature.group) && Objects.equals(value, feature.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group, value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("group", this.group)
        .add("value", this.value)
        .toString();
  }
}
