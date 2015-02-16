package io.getstream.client.model.feeds;

import io.getstream.client.repo.StreamRepository;

/**
 * Factory class to build a new instance of a feed.
 */
public final class BaseFeedFactory implements FeedFactory {

    private final StreamRepository streamRepository;

    public BaseFeedFactory(final StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    @Override
    public Feed createFeed(final String feedSlug, final String id) {
        return new BaseFeed(streamRepository, feedSlug, id);
    }
}
