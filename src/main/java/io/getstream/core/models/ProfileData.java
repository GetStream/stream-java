package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ProfileData {
    private final String id;
    private final int followingCount;
    private final int followersCount;
    private final Map<String, Object> data;

    @JsonCreator
    public ProfileData(
            @JsonProperty("id")
                    String id,
            @JsonProperty("following_count")
                    int followingCount,
            @JsonProperty("followers_count")
                    int followersCount,
            @JsonProperty("data")
                    Map<String, Object> data) {
        checkArgument(followersCount >= 0, "Can't have negative followers count");
        checkArgument(followingCount >= 0, "Can't have negative following count");
        this.id = checkNotNull(id, "ID required");
        this.followingCount = followingCount;
        this.followersCount = followersCount;
        this.data = checkNotNull(data, "Can't have null data");
    }

    public String getID() {
        return id;
    }

    @JsonProperty("followers_count")
    public int getFollowersCount() {
        return followersCount;
    }

    @JsonProperty("following_count")
    public int getFollowingCount() {
        return followingCount;
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    public <T> T get(String key) {
        return (T) data.get(checkNotNull(key, "Key can't be null"));
    }

    @JsonAnySetter
    public <T> ProfileData set(String key, T value) {
        checkArgument(!"id".equals(key), "Key can't be named 'id'");
        checkNotNull(key, "Key can't be null");

        data.put(key, value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileData data = (ProfileData) o;
        return Objects.equals(id, data.id) &&
                followersCount == data.followersCount &&
                followingCount == data.followingCount &&
                Objects.equals(data, data.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("data", this.data)
                .toString();
    }
}
