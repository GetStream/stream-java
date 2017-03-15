package io.getstream.client.okhttp;

import io.getstream.client.exception.InvalidFeedNameException;
import io.getstream.client.okhttp.repo.StreamRepoFactoryImpl;
import com.google.common.base.Preconditions;
import io.getstream.client.StreamClient;
import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.feeds.BaseFeedFactory;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.feeds.FeedFactory;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamClientImpl implements StreamClient {

    private FeedFactory feedFactory;
    private final StreamRepository streamRepository;

    public StreamClientImpl(final ClientConfiguration clientConfiguration, final String key, final String secretKey) {
        Preconditions.checkNotNull(clientConfiguration, "Client configuration cannot be null.");
        AuthenticationHandlerConfiguration authenticationHandlerConfiguration = new AuthenticationHandlerConfiguration();
        authenticationHandlerConfiguration.setApiKey(checkNotNull(key, "API key cannot be null."));
        authenticationHandlerConfiguration.setSecretKey(checkNotNull(secretKey, "API secret key cannot be null."));
        clientConfiguration.setAuthenticationHandlerConfiguration(authenticationHandlerConfiguration);
        this.streamRepository = new StreamRepoFactoryImpl().newInstance(clientConfiguration, authenticationHandlerConfiguration);
        this.feedFactory = new BaseFeedFactory(this.streamRepository);
    }

    @Override
    public Feed newFeed(final String feedSlug, final String id) throws InvalidFeedNameException {
        return this.feedFactory.createFeed(feedSlug, id);
    }

    protected FeedFactory getFeedFactory() {
        return feedFactory;
    }

    protected void setFeedFactory(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }

    @Override
    public void shutdown() throws IOException {
        this.streamRepository.shutdown();
    }
}