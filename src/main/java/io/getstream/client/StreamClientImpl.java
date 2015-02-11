package io.getstream.client;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.feeds.BaseFeedFactory;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.feeds.FeedFactory;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.repo.StreamRepositoryImpl;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamClientImpl implements StreamClient {

    private final AuthenticationHandlerConfiguration authenticationHandlerConfiguration;

    private final StreamRepository streamRepository;

	private final CloseableHttpClient httpClient;

    private FeedFactory feedFactory;

    public StreamClientImpl(final ClientConfiguration clientConfiguration, final String key, final String secretKey) {
        checkNotNull(clientConfiguration, "Client configuration cannot be null.");
        this.authenticationHandlerConfiguration = new AuthenticationHandlerConfiguration();
        this.authenticationHandlerConfiguration.setApiKey(checkNotNull(key, "API key cannot be null."));
        this.authenticationHandlerConfiguration.setSecretKey(checkNotNull(secretKey, "API secret key cannot be null."));
        clientConfiguration.setAuthenticationHandlerConfiguration(authenticationHandlerConfiguration);
		this.httpClient = initClient(clientConfiguration);
		this.streamRepository = new StreamRepositoryImpl(clientConfiguration, httpClient);
        this.feedFactory = new BaseFeedFactory(streamRepository);
    }

	private CloseableHttpClient initClient(final ClientConfiguration config) {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
				config.getTimeToLive(), TimeUnit.MILLISECONDS);
		connectionManager.setDefaultMaxPerRoute(config.getMaxConnections());
		connectionManager.setMaxTotal(config.getMaxConnections());
		return HttpClients.custom()
				.setDefaultRequestConfig(RequestConfig.custom()
						.setConnectTimeout(config.getConnectionTimeout())
						.setSocketTimeout(config.getTimeout()).build())
				.setMaxConnPerRoute(config.getMaxConnections())
				.setMaxConnTotal(config.getMaxConnections())
				.setConnectionManager(connectionManager).build();
	}

    @Override
	public Feed newFeed(final String feedSlug, final String id) {
        return this.feedFactory.createFeed(feedSlug, id);
    }

	@Override
	public void shutdown() throws IOException {
		this.httpClient.close();
	}

    protected FeedFactory getFeedFactory() {
        return feedFactory;
    }

    protected void setFeedFactory(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }
}