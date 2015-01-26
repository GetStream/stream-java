package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepositoryRestImpl;

public class FlatFeed extends BaseFeed {
    public FlatFeed(StreamRepositoryRestImpl streamRepository, String feedSlug, String id) {
        super(Feed.FLAT, streamRepository, feedSlug, id);
    }
}
