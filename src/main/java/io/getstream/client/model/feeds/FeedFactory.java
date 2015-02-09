package io.getstream.client.model.feeds;

import io.getstream.client.model.feeds.Feed;

public interface FeedFactory {
    Feed createFeed(String feedSlug, String id);
}
