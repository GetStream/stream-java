package io.getstream.client;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.feeds.BaseFeedFactory;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.feeds.FeedFactory;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.repo.StreamRepositoryImpl;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamClient {

    private final AuthenticationHandlerConfiguration authenticationHandlerConfiguration;

    private final StreamRepository streamRepository;

    private FeedFactory feedFactory;

    public StreamClient(final ClientConfiguration clientConfiguration, final String key, final String secretKey) {
        checkNotNull(clientConfiguration, "Client configuration cannot be null.");
        this.authenticationHandlerConfiguration = new AuthenticationHandlerConfiguration();
        this.authenticationHandlerConfiguration.setApiKey(checkNotNull(key, "API key cannot be null."));
        this.authenticationHandlerConfiguration.setSecretKey(checkNotNull(secretKey, "API secret key cannot be null."));
        clientConfiguration.setAuthenticationHandlerConfiguration(authenticationHandlerConfiguration);
		this.streamRepository = new StreamRepositoryImpl(clientConfiguration);
        this.feedFactory = new BaseFeedFactory(streamRepository);
    }

    public Feed newFeed(final String feedSlug, final String id) {
        return this.feedFactory.createFeed(feedSlug, id);
    }

	public StreamRepository getStreamRepository() {
		return streamRepository;
    }

    protected FeedFactory getFeedFactory() {
        return feedFactory;
    }

    protected void setFeedFactory(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }
}