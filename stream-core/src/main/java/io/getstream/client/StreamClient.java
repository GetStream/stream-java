package io.getstream.client;

import io.getstream.client.exception.InvalidFeedNameException;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.feeds.PersonalizedFeed;

import java.io.IOException;

/**
 * Basic interface to
 */
public interface StreamClient {

    /**
     * Get a new instance of the Feed.
     *
     * @param feedSlug Feed slug
     * @param id Feed id
     * @return A new feed
     * @throws InvalidFeedNameException if the feed name is not valid
     */
    Feed newFeed(String feedSlug, String id) throws InvalidFeedNameException;

    /**
     * Get a new Personalized feed
     *
     * @param feedSlug Feed slug
     * @param id Feed id
     * @return A new feed
     * @throws InvalidFeedNameException if the feed name is not valid
     */
    PersonalizedFeed newPersonalizedFeed(String feedSlug, String id) throws InvalidFeedNameException;

    /**
     * Send the shutdown signal to the client.
     *
     * @throws IOException in case of network/socket exceptions
     */
    void shutdown() throws IOException;
}
