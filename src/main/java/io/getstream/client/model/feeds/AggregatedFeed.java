package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepository;

public class AggregatedFeed extends BaseFeed implements Feed {
    public AggregatedFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(streamRepository, feedSlug, userId);
    }
}
