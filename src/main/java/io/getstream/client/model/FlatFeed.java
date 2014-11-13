package io.getstream.client.model;

import io.getstream.client.service.StreamRepository;

public class FlatFeed extends BaseFeed {
    public FlatFeed(StreamRepository streamRepository, String feedSlug, String id) {
        super(Feed.FLAT, streamRepository, feedSlug, id);
    }
}
