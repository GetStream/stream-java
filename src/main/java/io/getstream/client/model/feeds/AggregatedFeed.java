package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepository;

public class AggregatedFeed extends BaseFeed {
    public AggregatedFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(Feed.AGGREGATED, streamRepository, feedSlug, userId);
    }
}
