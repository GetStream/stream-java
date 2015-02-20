package io.getstream.client.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.handlers.StreamExceptionHandler;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.utils.SignatureUtils;
import io.getstream.client.utils.UriBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Collections;
import java.util.List;

import static io.getstream.client.utils.SignatureUtils.addSignatureToRecipients;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * Actual implementation of the Stream's REST API calls.
 */
public class StreamRepositoryImpl implements StreamRepository {

    private static final Logger LOG = LoggerFactory.getLogger(StreamRepositoryImpl.class);

    private static final String API_KEY = "api_key";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setPropertyNamingStrategy(
					/* will convert camelStyle to lower_case_style */
					PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    private final URI baseEndpoint;
    private final String apiKey;
	private final String secretKey;
    private final StreamExceptionHandler exceptionHandler;

	private CloseableHttpClient httpClient;

	/**
	 * Create a new {@link StreamRepository} using the given configuration {@link ClientConfiguration} and
	 * a pre-instantiated HttpClient {@link CloseableHttpClient}.
	 * @param streamClient
	 * @param closeableHttpClient
	 */
	public StreamRepositoryImpl(ClientConfiguration streamClient, CloseableHttpClient closeableHttpClient) {
		this.baseEndpoint = streamClient.getRegion().getEndpoint();
		this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
		this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
        this.exceptionHandler = new StreamExceptionHandler(OBJECT_MAPPER);
        this.httpClient = closeableHttpClient;
    }

    @Override
	public <T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException {
		HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(API_KEY, apiKey).build());
		LOG.debug("Invoking url: '{}'", request.getURI());

		addSignatureToRecipients(secretKey, activity);

		request.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(activity), APPLICATION_JSON));
		try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
			handleResponseCode(response);
			return OBJECT_MAPPER.readValue(response.getEntity().getContent(),
					OBJECT_MAPPER.getTypeFactory().constructType(activity.getClass()));
		}
	}

	@Override
	public <T extends BaseActivity> List<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		HttpGet request = new HttpGet(filter.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(API_KEY, apiKey)).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		return fetchActivities(addAuthentication(feed, request), OBJECT_MAPPER.getTypeFactory().constructParametricType(StreamResponse.class, type));
	}

	@Override
	public <T extends BaseActivity> List<AggregatedActivity<T>> getAggregatedActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		HttpGet request = new HttpGet(filter.apply(UriBuilder.fromEndpoint(baseEndpoint)
                                                           .path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
                                                           .queryParam(API_KEY, apiKey)).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		return fetchActivities(addAuthentication(feed, request), OBJECT_MAPPER.getTypeFactory().constructParametricType(StreamResponse.class,
				OBJECT_MAPPER.getTypeFactory().constructParametricType(AggregatedActivity.class, type)));
	}

	@Override
	public <T extends BaseActivity> List<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		HttpGet request = new HttpGet(filter.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(API_KEY, apiKey)).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		return fetchActivities(addAuthentication(feed, request), OBJECT_MAPPER.getTypeFactory().constructParametricType(StreamResponse.class,
				OBJECT_MAPPER.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
	}

	@Override
	public <T extends BaseActivity> List<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, boolean markAsRead, boolean markAsSeen) throws IOException, StreamClientException {
		HttpGet request = new HttpGet(filter.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(API_KEY, apiKey)
				.queryParam("mark_read", Boolean.toString(markAsRead))
				.queryParam("mark_seen", Boolean.toString(markAsSeen))).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		return fetchActivities(addAuthentication(feed, request), OBJECT_MAPPER.getTypeFactory().constructParametricType(StreamResponse.class,
				OBJECT_MAPPER.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
	}

	@Override
	public <T extends BaseActivity> List<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException {
		HttpGet request = new HttpGet(filter.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(API_KEY, apiKey)).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		return fetchActivities(addAuthentication(feed, request), OBJECT_MAPPER.getTypeFactory().constructParametricType(StreamResponse.class,
				OBJECT_MAPPER.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
	}

	private <T> List<T> fetchActivities(HttpRequestBase request, JavaType javaType) throws IOException, StreamClientException {
		try (CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create())) {
			handleResponseCode(response);
			StreamResponse<T> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(), javaType);
			return streamResponse.getResults();
		}
	}

	@Override
	public void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException {
        HttpDelete request = new HttpDelete(UriBuilder.fromEndpoint(baseEndpoint)
                                              .path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path(activityId + "/")
                                              .queryParam(API_KEY, apiKey).build());
		fireAndForget(addAuthentication(feed, request));
    }

    @Override
	public void follow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException {
        HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
                                              .path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following/")
                                              .queryParam(API_KEY, apiKey).build());

        request.setEntity(new UrlEncodedFormEntity(
				Collections.singletonList(new BasicNameValuePair("target", targetFeedId))));
		fireAndForget(addAuthentication(feed, request));
    }

    @Override
	public void unfollow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException {
        HttpDelete request = new HttpDelete(UriBuilder.fromEndpoint(baseEndpoint)
                                                .path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following").path(targetFeedId + "/")
                                                .queryParam(API_KEY, apiKey).build());
		fireAndForget(addAuthentication(feed, request));
    }

    @Override
	public List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
        HttpGet request = new HttpGet(filter.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following/")
                .queryParam(API_KEY, apiKey)).build());
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
			handleResponseCode(response);
            StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
                                                   new TypeReference<StreamResponse<FeedFollow>>(){});
            return streamResponse.getResults();
        }
    }

    @Override
	public List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
        HttpGet request = new HttpGet(filter.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("followers/")
                .queryParam(API_KEY, apiKey)).build());
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
			handleResponseCode(response);
            StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
					new TypeReference<StreamResponse<FeedFollow>>() {});
            return streamResponse.getResults();
        }
	}

	private void fireAndForget(final HttpRequestBase request) throws IOException, StreamClientException {
		LOG.debug("Invoking url: '{}", request.getURI());
		try (CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create())) {
			handleResponseCode(response);
		}
	}

	private void handleResponseCode(CloseableHttpResponse response) throws StreamClientException, IOException {
        exceptionHandler.handleResponseCode(response);
	}

    private HttpRequestBase addAuthentication(BaseFeed feed, HttpRequestBase httpRequest) {
        String tokenId = feed.getFeedSlug().concat(feed.getUserId());
        try {
            httpRequest.addHeader("Authorization", String.format("%s %s", tokenId, SignatureUtils.calculateHMAC(secretKey, tokenId)));
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException("Fatal error: cannot create authentication token.");
        }
        return httpRequest;
    }

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}
}
