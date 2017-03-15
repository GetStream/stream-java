package io.getstream.client.model.activities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.getstream.client.util.DateDeserializer;

import java.util.Date;
import java.util.List;

public abstract class WrappedActivity<T extends BaseActivity> {

    private List<T> activities;
    private long activityCount;
    private long actorCount;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    private String group;
    private String id;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date updatedAt;
    private String verb;

    public List<T> getActivities() {
        return activities;
    }

    public void setActivities(List<T> activities) {
        this.activities = activities;
    }

    public long getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(long activityCount) {
        this.activityCount = activityCount;
    }

    public long getActorCount() {
        return actorCount;
    }

    public void setActorCount(long actorCount) {
        this.actorCount = actorCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }
}
