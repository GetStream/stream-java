package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import io.getstream.core.models.serialization.DateDeserializer;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationGroup<T> extends Group<T> {
  private final boolean seen;
  private final boolean read;

  @JsonCreator
  public NotificationGroup(
      @JsonProperty("id") String id,
      @JsonProperty("group") String group,
      @JsonProperty("activities") List<T> activities,
      @JsonProperty("actor_count") int actorCount,
      @JsonProperty("created_at") @JsonDeserialize(using = DateDeserializer.class) Date createdAt,
      @JsonProperty("updated_at") @JsonDeserialize(using = DateDeserializer.class) Date updatedAt,
      @JsonProperty("is_seen") boolean isSeen,
      @JsonProperty("is_read") boolean isRead) {
    super(id, group, activities, actorCount, createdAt, updatedAt);

    this.seen = isSeen;
    this.read = isRead;
  }

  public boolean isSeen() {
    return seen;
  }

  public boolean isRead() {
    return read;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    NotificationGroup that = (NotificationGroup) o;
    return seen == that.seen && read == that.read;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), seen, read);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("activities", getActivities())
        .add("actorCount", getActorCount())
        .add("createdAt", getCreatedAt())
        .add("updatedAt", getUpdatedAt())
        .add("isSeen", seen)
        .add("isRead", read)
        .toString();
  }
}
