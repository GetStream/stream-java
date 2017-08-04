package io.getstream.client.okhttp.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.PersonalizedActivity;
import io.getstream.client.model.beans.MetaResponse;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.PersonalizedFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.okhttp.repo.handlers.StreamExceptionHandler;
import io.getstream.client.okhttp.repo.utils.StreamRepoUtils;
import io.getstream.client.okhttp.repo.utils.UriBuilder;
import io.getstream.client.repo.StreamPersonalizedRepository;
import io.getstream.client.util.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static io.getstream.client.okhttp.repo.utils.FeedFilterUtils.apply;
import static io.getstream.client.util.JwtAuthenticationUtil.ALL;
import static io.getstream.client.util.JwtAuthenticationUtil.generateToken;

public class StreamPersonalizedRepositoryImpl implements StreamPersonalizedRepository {

    private static final Logger LOG = LoggerFactory.getLogger(StreamRepositoryImpl.class);

    private static final String API_KEY = "api_key";
    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    private final ObjectMapper objectMapper;
    private final URI baseEndpoint;
    private final String apiKey;
    private final String secretKey;
    private final StreamExceptionHandler exceptionHandler;

    private final OkHttpClient httpClient;

    public StreamPersonalizedRepositoryImpl(ObjectMapper objectMapper,
                                            ClientConfiguration streamClient,
                                            OkHttpClient closeableHttpClient) {
        this.objectMapper = objectMapper;
        this.baseEndpoint = EndpointUtil.getPersonalizedEndpoint(streamClient);
        this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
        this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
        this.exceptionHandler = new StreamExceptionHandler(objectMapper);
        this.httpClient = closeableHttpClient;
    }

    @Override
    public <T extends PersonalizedActivity> List<T> get(PersonalizedFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
        Request.Builder requestBuilder = new Request.Builder().url(
                apply(UriBuilder.fromEndpoint(baseEndpoint)
                .path("personalized_feed/")
                .path(feed.getUserId().concat("/"))
                .queryParam(API_KEY, apiKey), filter).build().toURL()).get();

        Request request = StreamRepoUtils.addJwtAuthentication(
                generateToken(secretKey, ALL, ALL, null, feed.getUserId()),
                requestBuilder).build();
        LOG.debug("Invoking url: '{}", request.urlString());

        Response response = httpClient.newCall(request).execute();
        handleResponseCode(response);
        StreamResponse<T> streamResponse = objectMapper.readValue(response.body().byteStream(),
                objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, type));
        return streamResponse.getResults();
    }

    @Override
    public MetaResponse addMeta(PersonalizedFeed feed, Serializable metaPayload) throws IOException, StreamClientException {
        double duration = 0L;

        Request.Builder requestBuilder = new Request.Builder().url(UriBuilder.fromEndpoint(baseEndpoint)
                .path("meta/")
                .queryParam(API_KEY, apiKey).build().toURL());

        requestBuilder.post(RequestBody.create(MediaType.parse(APPLICATION_JSON),
                objectMapper.writeValueAsBytes(Collections.singletonMap("data",
                        Collections.singletonMap(String.format("%s:%s", feed.getSlug(), feed.getUserId()), metaPayload)))));

        Request request = StreamRepoUtils.addJwtAuthentication(
                generateToken(secretKey, ALL, ALL, null, feed.getUserId()), requestBuilder).build();

        LOG.debug("Invoking url: '{}", request.urlString());

        Response response = httpClient.newCall(request).execute();
        handleResponseCode(response);

        StreamResponse responseValue = objectMapper.readValue(response.body().byteStream(), StreamResponse.class);
        if (responseValue != null) {
            duration = Double.parseDouble(responseValue.getDuration());
        }
        return new MetaResponse(duration, response.code());
    }

    @Override
    public <T extends Serializable> List<T> getInterest(PersonalizedFeed feed, Class<T> type) throws IOException, StreamClientException {
        Request.Builder requestBuilder = new Request.Builder().url(
                UriBuilder.fromEndpoint(baseEndpoint)
                        .path("taste/")
                        .path(feed.getUserId().concat("/"))
                        .queryParam(API_KEY, apiKey).build().toURL()).get();

        Request request = StreamRepoUtils.addJwtAuthentication(
                generateToken(secretKey, ALL, ALL, null, feed.getUserId()),
                requestBuilder).build();
        LOG.debug("Invoking url: '{}", request.urlString());

        Response response = httpClient.newCall(request).execute();
        handleResponseCode(response);
        StreamResponse<T> streamResponse = objectMapper.readValue(response.body().byteStream(),
                objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, type));
        return streamResponse.getResults();
    }

    private void handleResponseCode(Response response) throws StreamClientException, IOException {
        exceptionHandler.handleResponseCode(response);
    }
}
