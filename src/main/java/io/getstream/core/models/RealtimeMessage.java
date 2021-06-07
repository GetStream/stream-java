package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.OptBoolean;

import java.util.Date;
import java.util.List;

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
}
