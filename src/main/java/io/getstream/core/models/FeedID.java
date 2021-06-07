package io.getstream.core.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Objects;

@JsonSerialize(using = ToStringSerializer.class)
public final class FeedID {
    private final String slug;
    private final String userID;

    public FeedID(String slug, String userID) {
        checkNotNull(slug, "Feed slug can't be null");
        checkArgument(!slug.contains(":"), "Invalid slug");
        checkNotNull(userID, "Feed user ID can't be null");
        checkArgument(!userID.contains(":"), "Invalid user ID");

        this.slug = slug;
        this.userID = userID;
    }

    public FeedID(String id) {
        checkNotNull(id, "Feed ID can't be null");
        checkArgument(id.contains(":"), "Invalid feed ID");

        String[] parts = id.split(":");
        checkArgument(parts.length == 2, "Invalid feed ID");
        this.slug = parts[0];
        this.userID = parts[1];
    }

    public String getSlug() {
        return slug;
    }

    public String getUserID() {
        return userID;
    }

    public String getClaim() {
        return slug + userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedID feedID = (FeedID) o;
        return Objects.equals(slug, feedID.slug) && Objects.equals(userID, feedID.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug, userID);
    }

    @Override
    public String toString() {
        return slug + ':' + userID;
    }
}
