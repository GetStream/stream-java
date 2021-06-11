package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.google.common.base.MoreObjects;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RealtimeMessage {
    private final FeedID feed;
    private final String appID;
    private final List<String> deleted;
    private final List<EnrichedActivity> newActivities;
    private final Date publishedAt;

    @JsonCreator
    public RealtimeMessage(
            @JsonProperty("feed") FeedID feed,
            @JsonProperty("app_id") String appID,
            @JsonProperty("deleted") List<String> deleted,
            @JsonProperty("new") List<EnrichedActivity> newActivities,
            @JsonProperty("published_at") Date publishedAt
    ) {
        this.feed = feed;
        this.appID = appID;
        this.deleted = deleted;
        this.newActivities = newActivities;
        this.publishedAt = publishedAt;
    }


    public FeedID getFeed() {
        return feed;
    }

    @JsonProperty("app_id")
    public String getAppID() {
        return appID;
    }

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
            lenient = OptBoolean.FALSE,
            timezone = "UTC")
    public List<String> getDeleted() {
        return deleted;
    }

    @JsonProperty("new")
    public List<EnrichedActivity> getNewActivities() {
        return newActivities;
    }

    @JsonProperty("published_at")
    public Date getPublishedAt() {
        return publishedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealtimeMessage message = (RealtimeMessage) o;
        return Objects.equals(feed, message.feed)
                && Objects.equals(appID, message.appID)
                && Objects.equals(deleted, message.deleted)
                && Objects.equals(newActivities, message.newActivities)
                && Objects.equals(publishedAt, message.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                feed,
                appID,
                deleted,
                newActivities,
                publishedAt
        );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("feed", this.feed)
                .add("appID", this.appID)
                .add("deleted", this.deleted)
                .add("new", this.newActivities)
                .add("publishedAt", this.publishedAt)
                .toString();
    }
}
