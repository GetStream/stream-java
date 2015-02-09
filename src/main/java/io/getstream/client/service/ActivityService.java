package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;
import java.util.List;

public interface ActivityService<T extends BaseActivity> {

    List<T> getActivities() throws IOException, StreamClientException;

    List<T> getActivities(final FeedFilter filter) throws IOException, StreamClientException;

    T addActivity(T activity) throws IOException, StreamClientException;
}
