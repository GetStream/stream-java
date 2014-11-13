package io.getstream.client.model;

import io.getstream.client.service.StreamRepository;

public class NotificationFeed extends BaseFeed {
    public NotificationFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(Feed.NOTIFICATION, streamRepository, feedSlug, userId);
    }
}
