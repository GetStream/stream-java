package io.getstream.client;

import io.getstream.client.model.feeds.Feed;

import java.io.IOException;

public interface StreamClient {
	Feed newFeed(String feedSlug, String id);

	void shutdown() throws IOException;
}
