package io.getstream.client;

import io.getstream.client.model.AggregatedFeed;
import io.getstream.client.model.Feed;
import io.getstream.client.model.FlatFeed;
import io.getstream.client.model.NotificationFeed;
import io.getstream.client.model.UserFeed;
import io.getstream.client.service.StreamRepository;

import java.io.IOException;

public final class FeedFactory {

    private final StreamRepository streamRepository;

    public FeedFactory(final StreamClient streamClient) {
        this.streamRepository = streamClient.getStreamRepository();
    }

    public FlatFeed createFlatFeed(final String feedSlug, final String id) {
        return new FlatFeed(streamRepository, feedSlug, id);
    }

    public NotificationFeed createNotificationFeed() {
        return new NotificationFeed(streamRepository);
    }

    public AggregatedFeed createAggregatedFeed() {
        return new AggregatedFeed(streamRepository);
    }

	public AggregatedFeed getAggregatedFeed(final String feedId) throws IOException {
		return (AggregatedFeed) streamRepository.getFeed(Feed.AGGREGATED, feedId);
	}

}
