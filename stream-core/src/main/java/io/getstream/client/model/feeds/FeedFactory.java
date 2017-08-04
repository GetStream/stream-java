package io.getstream.client.model.feeds;

import io.getstream.client.exception.InvalidFeedNameException;

/**
 * Factory class to create new feed.
 */
public interface FeedFactory {

    /**
     * Validation pattern for feed slug.
     */
    String FEED_SLUG_ALLOWED_PATTERN = "\\w+";

    /**
     * Validation patter for feed id.
     */
    String FEED_ID_ALLOWED_PATTERN = "[\\w-]+";

    /**
     * Create new feed.
     *
     * @param feedSlug feed slug.
     * @param id feed id.
     * @return A new feed
     * @throws InvalidFeedNameException if the name of the feed is not valid
     */
    Feed createFeed(String feedSlug, String id) throws InvalidFeedNameException;

    /**
     * Create new personalized feed.
     *
     * @param feedSlug feed slug.
     * @param id feed id.
     * @return A new feed
     * @throws InvalidFeedNameException if the name of the feed is not valid
     */
    PersonalizedFeed createPersonalizedFeed(String feedSlug, String id) throws InvalidFeedNameException;
}
