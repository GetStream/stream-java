package io.getstream.client.model.feeds;

import io.getstream.client.service.StreamRepositoryRestImpl;

public class AggregatedFeed extends BaseFeed {
    public AggregatedFeed(final StreamRepositoryRestImpl streamRepository, String feedSlug, String userId) {
        super(Feed.AGGREGATED, streamRepository, feedSlug, userId);
    }
}
