package io.getstream.client.model.beans;

import io.getstream.client.model.activities.BaseActivity;

import java.util.List;

/**
 * Response from activities' bulk insert/update.
 * @param <T> Type of the activity
 */
public class StreamActivitiesResponse<T extends BaseActivity> {

    private List<T> activities;

    public List<T> getActivities() {
        return activities;
    }

    public void setActivities(List<T> activities) {
        this.activities = activities;
    }
}
