package io.getstream.client;

import io.getstream.client.model.feeds.Feed;

import java.io.IOException;

/**
 * Basic interface to
 */
public interface StreamClient {

    /**
     * Get a new instance of the Feed.
     * @param feedSlug
     * @param id
     * @return
     */
	Feed newFeed(String feedSlug, String id);

    /**
     * Send the shutdown signal to the client.
     * @throws IOException
     */
	void shutdown() throws IOException;
}
