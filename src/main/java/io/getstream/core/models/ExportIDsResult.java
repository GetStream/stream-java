package io.getstream.core.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExportIDsResult {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("activity_count")
    private int activityCount;

    @JsonProperty("activity_ids")
    private List<String> activityIds;

    @JsonProperty("reaction_count")
    private int reactionCount;

    @JsonProperty("reaction_ids")
    private List<String> reactionIds;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public List<String> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<String> activityIds) {
        this.activityIds = activityIds;
    }

    public int getReactionCount() {
        return reactionCount;
    }

    public void setReactionCount(int reactionCount) {
        this.reactionCount = reactionCount;
    }

    public List<String> getReactionIds() {
        return reactionIds;
    }

    public void setReactionIds(List<String> reactionIds) {
        this.reactionIds = reactionIds;
    }
}