package io.getstream.client;

import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.BaseFeed;
import io.getstream.client.model.Feed;
import io.getstream.client.service.StreamRepository;
import io.getstream.client.service.StreamRepositoryRestImpl;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamClient {

    public static final String BASE_ENDPOINT = "https://getstream.io/api";

    private final String key;
    private final String secretKey;

    private final StreamRepository streamRepository;

    public StreamClient(final ClientConfiguration clientConfiguration, final String key, final String secretKey) {
        checkNotNull(clientConfiguration, "Client configuration cannot be null.");
        this.key = checkNotNull(key, "API key cannot be null.");
        this.secretKey = checkNotNull(secretKey, "API secret key cannot be null.");
		this.streamRepository = new StreamRepositoryRestImpl(clientConfiguration);
    }

    public FeedFactory getFeedFactory() {
        return new FeedFactory(this);
    }

    public BaseFeed getFeed(Feed feedType, String user, final String id) throws IOException {
        return this.streamRepository.getFeed(feedType, id);
    }

	public StreamRepository getStreamRepository() {
		return streamRepository;
	}
}