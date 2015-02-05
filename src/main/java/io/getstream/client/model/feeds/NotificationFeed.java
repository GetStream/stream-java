package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepository;

public class NotificationFeed extends BaseFeed implements Feed {

    public NotificationFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(streamRepository, feedSlug, userId);
    }

}
