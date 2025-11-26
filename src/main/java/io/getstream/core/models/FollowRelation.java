package io.getstream.core.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class FollowRelation {
  private final String source;
  private final String target;
  private final Integer activityCopyLimit;

  @JsonCreator
  public FollowRelation(
      @JsonProperty("feed_id") String source,
      @JsonProperty("target_id") String target,
      @JsonProperty("activity_copy_limit") Integer activityCopyLimit) {
    checkNotNull(source, "FollowRelation 'source' field required");
    checkNotNull(target, "FollowRelation 'target' field required");
    if (activityCopyLimit != null) {
      checkArgument(activityCopyLimit >= 0, "Activity copy limit must be non negative");
    }

    this.source = source;
    this.target = target;
    this.activityCopyLimit = activityCopyLimit;
  }

  public FollowRelation(
      @JsonProperty("feed_id") String source,
      @JsonProperty("target_id") String target) {
    this(source, target, null);
  }

  public String getSource() {
    return this.source;
  }

  public String getTarget() {
    return this.target;
  }

  @JsonProperty("activity_copy_limit")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Integer getActivityCopyLimit() {
    return this.activityCopyLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FollowRelation that = (FollowRelation) o;
    return Objects.equals(source, that.source)
        && Objects.equals(target, that.target)
        && Objects.equals(activityCopyLimit, that.activityCopyLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, target, activityCopyLimit);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("source", this.source)
        .add("target", this.target)
        .add("activityCopyLimit", this.activityCopyLimit)
        .toString();
  }
}
