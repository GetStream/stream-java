package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;
import java.util.List;

/**
 * Provide methods to interact with Notification activities of subtype of {@link BaseActivity}.
 * @param <T>
 */
public class NotificationActivityService<T extends BaseActivity> extends AbstractActivityService<T> {

    public NotificationActivityService(BaseFeed feed, Class type, StreamRepository streamRepository) {
        super(feed, type, streamRepository);
    }

	public List<NotificationActivity<T>> getNotificationActivities() throws IOException, StreamClientException {
		return streamRepository.getNotificationActivities(this.feed, this.type, new FeedFilter.Builder().build());
	}

	public List<NotificationActivity<T>> getNotificationActivities(final FeedFilter filter, final boolean markAsRead, final boolean markAsSeen) throws IOException, StreamClientException {
		return streamRepository.getNotificationActivities(this.feed, this.type, filter, markAsRead, markAsSeen);
	}

	public List<NotificationActivity<T>> getNotificationActivities(final FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException {
		return streamRepository.getNotificationActivities(this.feed, this.type, new FeedFilter.Builder().build(), markAsRead, markAsSeen);
	}
}
