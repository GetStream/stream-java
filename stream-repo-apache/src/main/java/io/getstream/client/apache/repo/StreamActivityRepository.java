package io.getstream.client.apache.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.apache.repo.handlers.StreamExceptionHandler;
import io.getstream.client.apache.repo.utils.SignatureUtils;
import io.getstream.client.apache.repo.utils.StreamRepoUtils;
import io.getstream.client.apache.repo.utils.UriBuilder;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.AddMany;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamActivitiesResponse;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.util.HttpSignatureHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static io.getstream.client.apache.repo.utils.FeedFilterUtils.apply;
import static io.getstream.client.util.JwtAuthenticationUtil.ALL;
import static io.getstream.client.util.JwtAuthenticationUtil.generateToken;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class StreamActivityRepository {

    private static final Logger LOG = LoggerFactory.getLogger(StreamActivityRepository.class);

    private final ObjectMapper objectMapper;
    private final URI baseEndpoint;
    private final String apiKey;
    private final StreamExceptionHandler exceptionHandler;
    private final CloseableHttpClient httpClient;
    private final String secretKey;

    public StreamActivityRepository(ObjectMapper objectMapper, URI baseEndpoint, String apiKey,
                                    StreamExceptionHandler exceptionHandler, CloseableHttpClient httpClient, String secretKey) {
        this.objectMapper = objectMapper;
        this.baseEndpoint = baseEndpoint;
        this.apiKey = apiKey;
        this.exceptionHandler = exceptionHandler;
        this.httpClient = httpClient;
        this.secretKey = secretKey;
    }

    public <T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException {
        HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
                                                .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                                                .queryParam(StreamRepositoryImpl.API_KEY, apiKey).build());
        LOG.debug("Invoking url: '{}'", request.getURI());

        SignatureUtils.addSignatureToRecipients(secretKey, activity);

        request.setEntity(new InputStreamEntity(new ByteArrayInputStream(objectMapper.writeValueAsBytes(activity)), APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                                                 objectMapper.getTypeFactory().constructType(activity.getClass()));
        }
    }

    public <T extends BaseActivity> StreamActivitiesResponse<T> addActivities(BaseFeed feed, Class<T> type, List<T> activities) throws StreamClientException, IOException {
        HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
                .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                .queryParam(StreamRepositoryImpl.API_KEY, apiKey).build());
        LOG.debug("Invoking url: '{}'", request.getURI());

        for (T activity : activities) {
            SignatureUtils.addSignatureToRecipients(secretKey, activity);
        }

        request.setEntity(new InputStreamEntity(new ByteArrayInputStream(
                objectMapper.writeValueAsBytes(Collections.singletonMap("activities", activities))), APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                    objectMapper.getTypeFactory().constructParametricType(StreamActivitiesResponse.class, type));
        }
    }

    public <T extends BaseActivity> T addToMany(List<String> targetIds, T activity) throws StreamClientException, IOException {
        HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
                .path("feed")
                .path("add_to_many/")
                .build());
        LOG.debug("Invoking url: '{}'", request.getURI());

        request.setEntity(new StringEntity(
                objectMapper.writeValueAsString(new AddMany<>(targetIds, activity)),
                APPLICATION_JSON));

        request.addHeader(HttpSignatureHandler.X_API_KEY_HEADER, apiKey);

        try (CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                    objectMapper.getTypeFactory().constructType(activity.getClass()));
        }
    }

    public <T extends BaseActivity> StreamResponse<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
        HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
                                                    .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                                                    .queryParam(StreamRepositoryImpl.API_KEY, apiKey), filter).build());
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                                                 objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, type));
        }
    }

    public <T extends BaseActivity> StreamResponse<AggregatedActivity<T>> getAggregatedActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
        HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
                                                    .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                                                    .queryParam(StreamRepositoryImpl.API_KEY, apiKey), filter).build());
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                                                 objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, objectMapper.getTypeFactory().constructParametricType(AggregatedActivity.class, type)));
        }
    }

    public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
        HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
                                                    .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                                                    .queryParam(StreamRepositoryImpl.API_KEY, apiKey), filter).build());
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                                                 objectMapper.getTypeFactory().constructParametricType(StreamResponse.class,
                                                                                                              objectMapper.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
        }
    }

    public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, boolean markAsRead, boolean markAsSeen) throws IOException, StreamClientException {
        HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
                                                    .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                                                    .queryParam(StreamRepositoryImpl.API_KEY, apiKey)
                                                    .queryParam("mark_read", Boolean.toString(markAsRead))
                                                    .queryParam("mark_seen", Boolean.toString(markAsSeen)), filter).build());
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                                                 objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, objectMapper.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
        }
    }

    public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException {
        UriBuilder baseUri = UriBuilder.fromEndpoint(baseEndpoint)
                                     .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                                     .queryParam(StreamRepositoryImpl.API_KEY, apiKey);
        if (null != markAsRead && markAsRead.hasActivities()) {
            baseUri.queryParam("mark_read", markAsRead.joinActivities());
        }
        if (null != markAsSeen && markAsSeen.hasActivities()) {
            baseUri.queryParam("mark_seen", markAsSeen.joinActivities());
        }
        HttpGet request = new HttpGet(apply(baseUri, filter).build());
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                                                 objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, objectMapper.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
        }
    }

    public void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException {
        HttpDelete request = new HttpDelete(UriBuilder.fromEndpoint(baseEndpoint)
                                                    .path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path(activityId + "/")
                                                    .queryParam(StreamRepositoryImpl.API_KEY, apiKey).build());
        LOG.debug("Invoking url: '{}", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
        }
    }

    public void deleteActivityByForeignId(BaseFeed feed, String activityId) throws IOException, StreamClientException {
        HttpDelete request = new HttpDelete(UriBuilder.fromEndpoint(baseEndpoint)
                .path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path(activityId + "/")
                .queryParam(StreamRepositoryImpl.API_KEY, apiKey)
                .queryParam("foreign_id", Boolean.toString(true))
                .build());
        LOG.debug("Invoking url: '{}", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
            handleResponseCode(response);
        }
    }

    public <T extends BaseActivity> StreamActivitiesResponse<T> updateActivities(BaseFeed feed, Class<T> type, List<T> activities) throws StreamClientException, IOException {
        HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
                .path("activities/")
                .queryParam(StreamRepositoryImpl.API_KEY, apiKey).build());
        LOG.debug("Invoking url: '{}'", request.getURI());


        request.setEntity(new InputStreamEntity(
                new ByteArrayInputStream(objectMapper.writeValueAsBytes(Collections.singletonMap("activities", activities))),
                APPLICATION_JSON)
        );

        request = StreamRepoUtils.addJwtAuthentication(generateToken(secretKey, ALL, "activities", ALL, null), request);

        try (CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create())) {
            handleResponseCode(response);
            return objectMapper.readValue(response.getEntity().getContent(),
                    objectMapper.getTypeFactory().constructParametricType(StreamActivitiesResponse.class, type));
        }
    }

    private HttpRequestBase addAuthentication(BaseFeed feed, HttpRequestBase request) {
        return StreamRepoUtils.addAuthentication(feed, secretKey, request);
    }

    private void handleResponseCode(CloseableHttpResponse response) throws StreamClientException, IOException {
        exceptionHandler.handleResponseCode(response);
    }
}
