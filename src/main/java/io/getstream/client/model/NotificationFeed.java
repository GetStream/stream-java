package io.getstream.client.model;

import io.getstream.client.service.StreamRepository;

public class NotificationFeed extends BaseFeed {
    public NotificationFeed(final StreamRepository streamRepository) {
        super(Feed.NOTIFICATION, streamRepository);
    }
}
