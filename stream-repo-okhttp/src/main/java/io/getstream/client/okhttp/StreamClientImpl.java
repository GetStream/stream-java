package io.getstream.client.okhttp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import io.getstream.client.StreamClient;
import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.InvalidFeedNameException;
import io.getstream.client.model.feeds.BaseFeedFactory;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.feeds.FeedFactory;
import io.getstream.client.model.feeds.PersonalizedFeed;
import io.getstream.client.okhttp.repo.HttpSignatureinterceptor;
import io.getstream.client.okhttp.repo.StreamPersonalizedRepositoryImpl;
import io.getstream.client.okhttp.repo.StreamRepositoryImpl;
import io.getstream.client.repo.StreamPersonalizedRepository;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.util.InfoUtil;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamClientImpl implements StreamClient {

    private static final String USER_AGENT_PREFIX = "stream-java-okhttp-%s";

    private final Optional<StreamPersonalizedRepository> streamPersonalizedRepository;
    private FeedFactory feedFactory;
    private final StreamRepository streamRepository;

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

        OkHttpClient httpClient = initClient(clientConfiguration, authenticationHandlerConfiguration);

        this.streamRepository = new StreamRepositoryImpl(OBJECT_MAPPER, clientConfiguration, httpClient);
        this.streamPersonalizedRepository = initPersonalizedRepo(clientConfiguration, httpClient);
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

    private OkHttpClient initClient(final ClientConfiguration config, AuthenticationHandlerConfiguration authConfig) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        client.setReadTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        client.setWriteTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        client.setRetryOnConnectionFailure(true);
        client.interceptors().add(new UserAgentInterceptor());
        client.interceptors().add(new HttpSignatureinterceptor(authConfig));
        client.setConnectionPool(new ConnectionPool(config.getMaxConnections(), config.getKeepAlive()));
        return client;
    }

    private Optional<StreamPersonalizedRepository> initPersonalizedRepo(ClientConfiguration config, OkHttpClient httpClient) {
        final Optional<String> endpoint = Optional.fromNullable(config.getPersonalizedFeedEndpoint());
        if (endpoint.isPresent()) {
            return Optional.<StreamPersonalizedRepository>of(new StreamPersonalizedRepositoryImpl(OBJECT_MAPPER, config, httpClient));
        }
        return Optional.absent();
    }

    /**
     * Add custom user-agent to the request.
     */
    class UserAgentInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().header("User-Agent", getUserAgent()).build());
        }
    }

    private String getUserAgent() {
        String version = "undefined";
        Properties properties = InfoUtil.getProperties();
        if (null != properties) {
            version = properties.getProperty(InfoUtil.VERSION);
        }
        return String.format(USER_AGENT_PREFIX, version);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}