package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * Provide methods to interact with Flat activities of subtype of {@link BaseActivity}.
 * @param <T>
 */
public interface FlatActivityService<T extends BaseActivity> {

    /**
     * List flat activities using a given filter.
     * @param filter
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    StreamResponse<T> getActivities(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List flat activities.
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    StreamResponse<T> getActivities() throws IOException, StreamClientException;
}
