package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepository;

public class FlatFeed extends BaseFeed implements Feed {
    public FlatFeed(StreamRepository streamRepository, String feedSlug, String id) {
        super(streamRepository, feedSlug, id);
    }
}
