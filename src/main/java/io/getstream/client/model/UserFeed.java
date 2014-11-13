package io.getstream.client.model;

import io.getstream.client.service.StreamRepository;

public class UserFeed extends BaseFeed {
    public UserFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(Feed.USER, streamRepository, feedSlug, userId);
    }
}
