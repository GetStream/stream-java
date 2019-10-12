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

import static com.google.common.base.Preconditions.checkNotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group<T> {
    private final String id;
    private final String group;
    private final List<T> activities;
    private final int actorCount;
    private final Date createdAt;
    private final Date updatedAt;

    @JsonCreator
    public Group(
            @JsonProperty("id")
                    String id,
            @JsonProperty("group")
                    String group,
            @JsonProperty("activities")
                    List<T> activities,
            @JsonProperty("actor_count")
                    int actorCount,
            @JsonProperty("created_at")
            @JsonDeserialize(using = DateDeserializer.class)
                    Date createdAt,
            @JsonProperty("updated_at")
            @JsonDeserialize(using = DateDeserializer.class)
                    Date updatedAt) {
        checkNotNull(id, "Group 'id' field required");
        checkNotNull(group, "Group 'group' field required");
        checkNotNull(activities, "Group 'activities' field required");

        this.id = id;
        this.group = group;
        this.activities = activities;
        this.actorCount = actorCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getID() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getGroupID() {
        return id + '.' + group;
    }

    public List<T> getActivities() {
        return activities;
    }

    public int getActorCount() {
        return actorCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group that = (Group) o;
        return actorCount == that.actorCount &&
                Objects.equals(id, that.id) &&
                Objects.equals(group, that.group) &&
                Objects.equals(activities, that.activities) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, activities, actorCount, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("activities", this.activities)
                .add("actorCount", this.actorCount)
                .add("createdAt", this.createdAt)
                .add("updatedAt", this.updatedAt)
                .toString();
    }
}
