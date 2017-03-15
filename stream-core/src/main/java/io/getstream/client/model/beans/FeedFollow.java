package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.getstream.client.util.DateDeserializer;

import java.util.Date;

/**
 * Contains follower/following-related data.
 */
public class FeedFollow {

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("feed_id")
    private String feedId;

    @JsonProperty("target_id")
    private String targetId;

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("updated_at")
    private Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
