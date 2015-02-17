package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;
import java.util.List;

/**
 * The {@link ActivityService} exposes operations to interact with activities of subtype of
 * {@link BaseActivity}.
 * @param <T> Type of the activity to deal with.
 */
public interface ActivityService<T extends BaseActivity> {

    /**
     * Lists the activities contained in the feed.
     * @return List of activities of type {@link T}
     * @throws IOException
     * @throws StreamClientException
     */
    List<T> getActivities() throws IOException, StreamClientException;

    /**
     * Lists the activities contained in the feed using the given filter.
     * @param filter Filter out the activities.
     * @return List of activities of type {@link T}
     * @throws IOException
     * @throws StreamClientException
     */
    List<T> getActivities(final FeedFilter filter) throws IOException, StreamClientException;

    /**
     * Add a new activity of type {@link T}.
     * @param activity Activity to add.
     * @return Response activity of type {@link T} coming from the server.<br/>
     *         The returning activity in the 'to' field contains the targetFeedId along
     *         with its signature (e.g: 'user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac')
     * @throws IOException
     * @throws StreamClientException
     */
    T addActivity(T activity) throws IOException, StreamClientException;
}
