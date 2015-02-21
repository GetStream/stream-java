package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * Provide methods to interact with Notification activities of subtype of {@link BaseActivity}.
 * @param <T>
 */
public interface NotificationActivityService<T extends BaseActivity> {

    /**
     * List notification activities using the given filter.
     * Futhermore, mark a list of given activities {@link MarkedActivity} as read and/or seen.
     * @param filter
     * @param markAsRead A list of activity ids to be marked as read.
     * @param markAsSeen A list of activity ids to be marked as seen.
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    StreamResponse<NotificationActivity<T>> getActivities(FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException;

    /**
     * List notification activities using the given filter.
     * Futhermore, mark all activities {@link MarkedActivity} as read and/or seen.
     * @param filter
     * @param markAsRead If true, mark all the activities as read. If false leave them untouched.
     * @param markAsSeen If true, mark all the activities as seen. If false leave them untouched.
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    StreamResponse<NotificationActivity<T>> getActivities(FeedFilter filter, boolean markAsRead, boolean markAsSeen) throws IOException, StreamClientException;

    /**
     * Get a list of activities using the standard filter (limit = 25).
     * @return
     * @throws IOException
     * @throws StreamClientException
     */
    StreamResponse<NotificationActivity<T>> getActivities() throws IOException, StreamClientException;
}
