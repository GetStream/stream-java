package io.getstream.client;

import io.getstream.client.model.feeds.AggregatedFeed;
import io.getstream.client.model.feeds.FlatFeed;
import io.getstream.client.model.feeds.NotificationFeed;
import io.getstream.client.service.StreamRepository;

public final class FeedFactory {

    private final StreamRepository streamRepository;

    public FeedFactory(final StreamClient streamClient) {
        this.streamRepository = streamClient.getStreamRepository();
    }

    public FlatFeed createFlatFeed(final String feedSlug, final String id) {
        return new FlatFeed(streamRepository, feedSlug, id);
    }

    public NotificationFeed createNotificationFeed(final String feedSlug, final String id) {
        return new NotificationFeed(streamRepository, feedSlug, id);
    }

    public AggregatedFeed createAggregatedFeed(final String feedSlug, final String id) {
        return new AggregatedFeed(streamRepository, feedSlug, id);
    }

}
