package io.getstream.client.model;

import io.getstream.client.service.StreamRepository;

public class AggregatedFeed extends BaseFeed<AggregatedActivity> {
    public AggregatedFeed(final StreamRepository streamRepository) {
        super(Feed.AGGREGATED, streamRepository);
    }
}
