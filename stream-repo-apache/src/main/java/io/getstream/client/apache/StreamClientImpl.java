package io.getstream.client.apache;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import io.getstream.client.StreamClient;
import io.getstream.client.apache.repo.HttpSignatureInterceptor;
import io.getstream.client.apache.repo.StreamPersonalizedRepositoryImpl;
import io.getstream.client.apache.repo.StreamRepositoryImpl;
import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.InvalidFeedNameException;
import io.getstream.client.model.feeds.BaseFeedFactory;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.feeds.FeedFactory;
import io.getstream.client.model.feeds.PersonalizedFeed;
import io.getstream.client.repo.StreamPersonalizedRepository;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.util.InfoUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamClientImpl implements StreamClient {

    private static final String USER_AGENT_PREFIX = "stream-java-apache-%s";

    private FeedFactory feedFactory;
    private final StreamRepository streamRepository;
    private final Optional<StreamPersonalizedRepository> streamPersonalizedRepository;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public StreamClientImpl(final ClientConfiguration clientConfiguration, final String key, final String secretKey) {
        Preconditions.checkNotNull(clientConfiguration, "Client configuration cannot be null.");
        AuthenticationHandlerConfiguration authenticationHandlerConfiguration = new AuthenticationHandlerConfiguration();
        authenticationHandlerConfiguration.setApiKey(checkNotNull(key, "API key cannot be null."));
        authenticationHandlerConfiguration.setSecretKey(checkNotNull(secretKey, "API secret key cannot be null."));
        clientConfiguration.setAuthenticationHandlerConfiguration(authenticationHandlerConfiguration);

        CloseableHttpClient closeableHttpClient = initClient(clientConfiguration, authenticationHandlerConfiguration);

        this.streamRepository = new StreamRepositoryImpl(OBJECT_MAPPER, clientConfiguration, closeableHttpClient);
        this.streamPersonalizedRepository = initPersonalizedRepo(clientConfiguration, closeableHttpClient);
        this.feedFactory = new BaseFeedFactory(this.streamRepository, this.streamPersonalizedRepository);
    }

    @Override
    public Feed newFeed(final String feedSlug, final String id) throws InvalidFeedNameException {
        return this.feedFactory.createFeed(feedSlug, id);
    }

    @Override
    public PersonalizedFeed newPersonalizedFeed(final String feedSlug,
                                                final String id) throws InvalidFeedNameException {
        return this.feedFactory.createPersonalizedFeed(feedSlug, id);
    }

    @Override
    public void shutdown() throws IOException {
        this.streamRepository.shutdown();
    }

    protected FeedFactory getFeedFactory() {
        return feedFactory;
    }

    protected void setFeedFactory(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }

    private Optional<StreamPersonalizedRepository> initPersonalizedRepo(ClientConfiguration config, CloseableHttpClient closeableHttpClient) {
        final Optional<String> endpoint = Optional.fromNullable(config.getPersonalizedFeedEndpoint());
        if (endpoint.isPresent()) {
            return Optional.<StreamPersonalizedRepository>of(new StreamPersonalizedRepositoryImpl(OBJECT_MAPPER, config, closeableHttpClient));
        }
        return Optional.absent();
    }

    private String getUserAgent() {
        String version = "undefined";
        Properties properties = InfoUtil.getProperties();
        if (null != properties) {
            version = properties.getProperty(InfoUtil.VERSION);
        }
        return String.format(USER_AGENT_PREFIX, version);
    }

    private CloseableHttpClient initClient(final ClientConfiguration config,
                                           AuthenticationHandlerConfiguration authConfig) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(config.getTimeToLive(),
                TimeUnit.MILLISECONDS);
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());
        connectionManager.setMaxTotal(config.getMaxConnections());

        return HttpClients.custom()
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setUserAgent(getUserAgent())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(config.getConnectionTimeout())
                        .setSocketTimeout(config.getTimeout()).build())
                .setMaxConnPerRoute(config.getMaxConnectionsPerRoute())
                .setMaxConnTotal(config.getMaxConnections())
                .setConnectionManager(connectionManager)
                .addInterceptorLast(new HttpSignatureInterceptor(authConfig))
                .setProxy(new HttpHost(config.getProxyHost(),config.getProxyPort()))
                .build();
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}