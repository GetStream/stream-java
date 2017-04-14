package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * Provide methods to interact with Aggregated activities of subtype of {@link BaseActivity}.
 *
 * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
 */
public interface AggregatedActivityService<T extends BaseActivity> {

    /**
     * List aggregated activities.
     *
     * @param filter Filter out the activities. Limited to 25 items by default.
     * @return List of aggregated activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    StreamResponse<AggregatedActivity<T>> getActivities(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List aggregated activities.
     *
     * @return List of aggregated activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    StreamResponse<AggregatedActivity<T>> getActivities() throws IOException, StreamClientException;
}
