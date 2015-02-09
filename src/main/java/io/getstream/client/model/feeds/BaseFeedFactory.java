package io.getstream.client.model.feeds;

import io.getstream.client.StreamClient;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.feeds.FeedFactory;
import io.getstream.client.repo.StreamRepository;

public final class BaseFeedFactory implements FeedFactory {

    private final StreamRepository streamRepository;

    public BaseFeedFactory(final StreamClient streamClient) {
        this.streamRepository = streamClient.getStreamRepository();
    }

    public BaseFeedFactory(final StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    @Override
    public Feed createFeed(final String feedSlug, final String id) {
        return new BaseFeed(streamRepository, feedSlug, id);
    }
}
