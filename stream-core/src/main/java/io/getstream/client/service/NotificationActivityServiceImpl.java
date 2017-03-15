package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * Provide methods to interact with Notification activities of subtype of {@link BaseActivity}.
 *
 * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
 */
public class NotificationActivityServiceImpl<T extends BaseActivity> extends AbstractActivityService<T>
        implements NotificationActivityService<T> {

    public NotificationActivityServiceImpl(BaseFeed feed, Class type, StreamRepository streamRepository) {
        super(feed, type, streamRepository);
    }

    @Override
    public StreamResponse<NotificationActivity<T>> getActivities() throws IOException, StreamClientException {
        return streamRepository.getNotificationActivities(this.feed, this.type, new FeedFilter.Builder().build());
    }

    @Override
    public StreamResponse<NotificationActivity<T>> getActivities(final FeedFilter filter, final boolean markAsRead, final boolean markAsSeen) throws IOException, StreamClientException {
        return streamRepository.getNotificationActivities(this.feed, this.type, filter, markAsRead, markAsSeen);
    }

    @Override
    public StreamResponse<NotificationActivity<T>> getActivities(final FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException {
        return streamRepository.getNotificationActivities(this.feed, this.type, new FeedFilter.Builder().build(), markAsRead, markAsSeen);
    }
}
