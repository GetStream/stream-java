package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.client.model.activities.BaseActivity;

import java.util.List;

/**
 * Helper bean used to create a payload for the add_to_many API call.
 * @param <T> Type of the activity
 */
public class AddMany<T extends BaseActivity>  {

    @JsonProperty("feeds")
    private List<String> targetIds;
    private T activity;

    public AddMany(List<String> targetIds, T activity) {
        this.targetIds = targetIds;
        this.activity = activity;
    }

    public List<String> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<String> targetIds) {
        this.targetIds = targetIds;
    }

    public T getActivity() {
        return activity;
    }

    public void setActivity(T activity) {
        this.activity = activity;
    }
}
