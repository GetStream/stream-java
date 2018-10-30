package io.getstream.client.model.activities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <b>update_to_targets</b>'s response wrapper class.
 * @param <T> Type of the activity in scope.
 */
public class UpdateTargetResponse<T extends BaseActivity> {

    private T activity;
    private String duration;

    @JsonProperty("added")
    private List<String> addedTargets;

    @JsonProperty("removed")
    private List<String> removedTargets;

    @JsonProperty("new")
    private List<String> newTargets;

    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Duration of the operation.
     * @return Duration in human-readable format.
     */
    public String getDuration() {
        return duration;
    }

    public T getActivity() {
        return activity;
    }

    public void setActivity(T activity) {
        this.activity = activity;
    }

    /**
     * Get a list of added target(s).
     * @return List of added target(s)
     */
    public List<String> getAddedTargets() {
        return addedTargets;
    }

    public void setAddedTargets(List<String> addedTargets) {
        this.addedTargets = addedTargets;
    }

    /**
     * Get a list of removed target(s).
     * @return List of remove target(s)
     */
    public List<String> getRemovedTargets() {
        return removedTargets;
    }

    public void setRemovedTargets(List<String> removedTargets) {
        this.removedTargets = removedTargets;
    }

    /**
     * Get a list of newly created target(s).
     * @return List of newly created target(s)
     */
    public List<String> getNewTargets() {
        return newTargets;
    }

    public void setNewTargets(List<String> newTargets) {
        this.newTargets = newTargets;
    }
}
