package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepositoryRestImpl;

public class NotificationFeed extends BaseFeed {
    public NotificationFeed(final StreamRepositoryRestImpl streamRepository, String feedSlug, String userId) {
        super(Feed.NOTIFICATION, streamRepository, feedSlug, userId);
    }
}
