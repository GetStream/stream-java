package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepositoryRestImpl;

public class UserFeed extends BaseFeed {
    public UserFeed(final StreamRepositoryRestImpl streamRepository, String feedSlug, String userId) {
        super(Feed.USER, streamRepository, feedSlug, userId);
    }
}
