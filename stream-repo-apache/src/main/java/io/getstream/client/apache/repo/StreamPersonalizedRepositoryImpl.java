package io.getstream.client.apache.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.apache.repo.handlers.StreamExceptionHandler;
import io.getstream.client.apache.repo.utils.StreamRepoUtils;
import io.getstream.client.apache.repo.utils.UriBuilder;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.PersonalizedActivity;
import io.getstream.client.model.beans.MetaResponse;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.PersonalizedFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamPersonalizedRepository;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.util.EndpointUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static io.getstream.client.apache.repo.utils.FeedFilterUtils.apply;
import static io.getstream.client.util.JwtAuthenticationUtil.ALL;
import static io.getstream.client.util.JwtAuthenticationUtil.generateToken;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class StreamPersonalizedRepositoryImpl implements StreamPersonalizedRepository {

    private static final Logger LOG = LoggerFactory.getLogger(StreamRepositoryImpl.class);

    static final String API_KEY = "api_key";

    private final ObjectMapper objectMapper;
    private final URI baseEndpoint;
    private final String apiKey;
    private final String secretKey;
    private final StreamExceptionHandler exceptionHandler;

    private final CloseableHttpClient httpClient;

    /**
     * Create a new {@link StreamRepository} using the given configuration {@link ClientConfiguration} and
     * a pre-instantiated HttpClient {@link CloseableHttpClient}.
     *
     * @param streamClient Client configuration
     * @param closeableHttpClient Actual instance of Apache client
     */
    public StreamPersonalizedRepositoryImpl(ObjectMapper objectMapper,
                                            ClientConfiguration streamClient,
                                            CloseableHttpClient closeableHttpClient) {
        this.objectMapper = objectMapper;
        this.baseEndpoint = EndpointUtil.getPersonalizedEndpoint(streamClient);
        this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
        this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
        this.exceptionHandler = new StreamExceptionHandler(objectMapper);
        this.httpClient = closeableHttpClient;
    }


    public <T extends PersonalizedActivity> List<T> get(PersonalizedFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
        HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
                .path("personalized_feed/")
                .path(feed.getUserId().concat("/"))
                .queryParam(API_KEY, apiKey), filter).build());
        LOG.debug("Invoking url: '{}'", request.getURI());

        request = StreamRepoUtils.addJwtAuthentication(generateToken(secretKey, ALL, ALL, null, feed.getUserId()), request);
        try (CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create())) {
            handleResponseCode(response);

            StreamResponse<T> streamResponse = objectMapper.readValue(response.getEntity().getContent(),
                    objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, type));
            return streamResponse.getResults();
        }
    }

    @Override
    public MetaResponse addMeta(PersonalizedFeed feed, Serializable metaPayload) throws IOException, StreamClientException {
        HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
                .path("meta/")
                .queryParam(API_KEY, apiKey).build());
        LOG.debug("Invoking url: '{}'", request.getURI());

        request.setEntity(new InputStreamEntity(new ByteArrayInputStream(
                objectMapper.writeValueAsBytes(Collections.singletonMap("data",
                        Collections.singletonMap(String.format("%s:%s", feed.getSlug(), feed.getUserId()), metaPayload)))), APPLICATION_JSON));

        request = StreamRepoUtils.addJwtAuthentication(generateToken(secretKey, ALL, ALL, null, feed.getUserId()), request);
        try (CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create())) {
            handleResponseCode(response);

            double duration = 0L;
            StreamResponse responseValue = objectMapper.readValue(response.getEntity().getContent(), StreamResponse.class);
            if (responseValue != null) {
                duration = Double.parseDouble(responseValue.getDuration());
            }
            return new MetaResponse(duration, response.getStatusLine().getStatusCode());
        }
    }

    @Override
    public <T extends Serializable> List<T> getInterest(PersonalizedFeed feed, Class<T> type) throws IOException, StreamClientException {
        HttpGet request = new HttpGet(UriBuilder.fromEndpoint(baseEndpoint)
                .path("taste/")
                .path(feed.getUserId().concat("/"))
                .queryParam(API_KEY, apiKey).build());
        LOG.debug("Invoking url: '{}'", request.getURI());

        request = StreamRepoUtils.addJwtAuthentication(generateToken(secretKey, ALL, ALL, null, feed.getUserId()), request);
        try (CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create())) {
            handleResponseCode(response);

            StreamResponse<T> streamResponse = objectMapper.readValue(response.getEntity().getContent(),
                    objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, type));
            return streamResponse.getResults();
        }
    }

    private void handleResponseCode(CloseableHttpResponse response) throws StreamClientException, IOException {
        exceptionHandler.handleResponseCode(response);
    }
}
