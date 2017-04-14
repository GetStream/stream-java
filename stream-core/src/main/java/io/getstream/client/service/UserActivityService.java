package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * Provide methods to interact with User activities of subtype of {@link BaseActivity}.
 *
 * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
 */
public interface UserActivityService<T extends BaseActivity> {

    /**
     * List user activities using the given filter.
     *
     * @param filter Filter to use to filter out the activities
     * @return List of user activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     * */
    StreamResponse<T> getActivities(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List user activities using the standard filter (limit = 25).
     *
     * @return List of user activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     * */
    StreamResponse<T> getActivities() throws IOException, StreamClientException;
}
