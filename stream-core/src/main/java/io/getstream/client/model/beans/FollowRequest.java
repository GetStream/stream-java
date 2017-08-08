package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FollowRequest {

    private String target;

    @JsonProperty("activity_copy_limit")
    private Integer activityCopyLimit;

    public FollowRequest() {

    }

    public FollowRequest(String target, Integer activityCopyLimit) {
        this.target = target;
        this.activityCopyLimit = activityCopyLimit;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getActivityCopyLimit() {
        return activityCopyLimit;
    }

    public void setActivityCopyLimit(Integer activityCopyLimit) {
        this.activityCopyLimit = activityCopyLimit;
    }
}
