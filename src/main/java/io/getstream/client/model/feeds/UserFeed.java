package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepository;

public class UserFeed extends BaseFeed implements Feed {
    public UserFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(streamRepository, feedSlug, userId);
    }
}
