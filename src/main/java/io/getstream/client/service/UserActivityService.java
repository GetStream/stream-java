package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * Provide methods to interact with User activities of subtype of {@link BaseActivity}.
 * @param <T>
 */
public interface UserActivityService<T extends BaseActivity> {

    /**
     * List user activities using the given filter.
     * @param filter
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    StreamResponse<T> getActivities(FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List user activities using the standard filter (limit = 25).
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    StreamResponse<T> getActivities() throws IOException, StreamClientException;
}
