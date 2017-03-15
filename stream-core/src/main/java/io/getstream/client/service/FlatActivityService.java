package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * Provide methods to interact with Flat activities of subtype of {@link BaseActivity}.
 *
 * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
 */
public interface FlatActivityService<T extends BaseActivity> {

    /**
     * List flat activities using a given filter.
     *
     * @param filter Filter to use to filter out the activities
     * @return List of flat activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    StreamResponse<T> getActivities(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List flat activities.
     *
     * @return List of flat activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     * */
    StreamResponse<T> getActivities() throws IOException, StreamClientException;
}
