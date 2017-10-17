package io.getstream.client.apache.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.apache.StreamClientImpl;
import io.getstream.client.apache.repo.handlers.StreamExceptionHandler;
import io.getstream.client.apache.repo.utils.StreamRepoUtils;
import io.getstream.client.apache.repo.utils.UriBuilder;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.FollowMany;
import io.getstream.client.model.beans.FollowRequest;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamActivitiesResponse;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.util.EndpointUtil;
import io.getstream.client.util.JwtAuthenticationUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static io.getstream.client.apache.repo.utils.FeedFilterUtils.apply;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * Actual implementation of the Stream's REST API calls.
 */
public class StreamRepositoryImpl implements StreamRepository {

	private static final Logger LOG = LoggerFactory.getLogger(StreamRepositoryImpl.class);

	static final String API_KEY = "api_key";

	private final ObjectMapper objectMapper;
	private final URI baseEndpoint;
	private final String apiKey;
	private final String secretKey;
	private final StreamExceptionHandler exceptionHandler;
	private final CloseableHttpClient httpClient;

	private final StreamActivityRepository streamActivityRepository;

	/**
	 * Create a new {@link StreamRepository} using the given configuration {@link ClientConfiguration} and
	 * a pre-instantiated HttpClient {@link CloseableHttpClient}.
	 *
	 * @param objectMapper Json Object Mapper
	 * @param streamClient Client configuration
	 * @param closeableHttpClient Actual instance of Apache client
	 */
	public StreamRepositoryImpl(ObjectMapper objectMapper, ClientConfiguration streamClient, CloseableHttpClient closeableHttpClient) {
		this.objectMapper = objectMapper;
		this.baseEndpoint = EndpointUtil.getBaseEndpoint(streamClient);
		this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
		this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
		this.exceptionHandler = new StreamExceptionHandler(objectMapper);
		this.httpClient = closeableHttpClient;
		this.streamActivityRepository = new StreamActivityRepository(objectMapper, baseEndpoint, apiKey, exceptionHandler,
				httpClient, secretKey);
	}

	@Override
	public String getReadOnlyToken(BaseFeed feed) {
		return JwtAuthenticationUtil.generateToken(secretKey, "read", "*", feed.getFeedSlug().concat(feed.getUserId()), null);
	}

	@Override
	public void follow(BaseFeed feed, String targetFeedId, int activityCopyLimit) throws StreamClientException, IOException {
		HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following/")
				.queryParam(API_KEY, apiKey).build());

		request.setEntity(new StringEntity(
						objectMapper.writeValueAsString(new FollowRequest(targetFeedId, activityCopyLimit)), APPLICATION_JSON));
		fireAndForget(addAuthentication(feed, request));
	}

	@Override
	public void followMany(BaseFeed feed, FollowMany followManyInput, int activityCopyLimit) throws StreamClientException, IOException {
		HttpPost request = new HttpPost(UriBuilder.fromEndpoint(baseEndpoint)
				.path("follow_many/")
				.queryParam("activity_copy_limit", activityCopyLimit)
				.build());
		request.addHeader(HttpSignatureInterceptor.X_API_KEY_HEADER, apiKey);
		request.setEntity(new StringEntity(objectMapper.writeValueAsString(followManyInput), APPLICATION_JSON));
		fireAndForget(request);
	}

	@Override
	public void unfollow(BaseFeed feed, String targetFeedId, boolean keepHistory) throws StreamClientException, IOException {
		HttpDelete request = new HttpDelete(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following").path(targetFeedId + "/")
				.queryParam("keep_history", Boolean.toString(keepHistory))
				.queryParam(API_KEY, apiKey).build());
		fireAndForget(addAuthentication(feed, request));
	}

	@Override
	public List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
		HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following/")
				.queryParam(API_KEY, apiKey), filter).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
			handleResponseCode(response);
			StreamResponse<FeedFollow> streamResponse = objectMapper.readValue(response.getEntity().getContent(),
					new TypeReference<StreamResponse<FeedFollow>>() {
					});
			return streamResponse.getResults();
		}
	}

	@Override
	public List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
		HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("followers/")
				.queryParam(API_KEY, apiKey), filter).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
			handleResponseCode(response);
			StreamResponse<FeedFollow> streamResponse = objectMapper.readValue(response.getEntity().getContent(),
					new TypeReference<StreamResponse<FeedFollow>>() {
					});
			return streamResponse.getResults();
		}
	}

	@Override
	public void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException {
		streamActivityRepository.deleteActivityById(feed, activityId);
	}

	@Override
	public void deleteActivityByForeignId(BaseFeed feed, String foreignId) throws IOException, StreamClientException {
		streamActivityRepository.deleteActivityByForeignId(feed, foreignId);
	}

	@Override
	public <T extends BaseActivity> StreamResponse<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		return streamActivityRepository.getActivities(feed, type, filter);
	}

	@Override
	public <T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException {
		return streamActivityRepository.addActivity(feed, activity);
	}

	@Override
	public <T extends BaseActivity> StreamActivitiesResponse<T> addActivities(BaseFeed feed, Class<T> type, List<T> activities) throws StreamClientException, IOException {
		return streamActivityRepository.addActivities(feed, type, activities);
	}

	@Override
	public <T extends BaseActivity> StreamActivitiesResponse<T> updateActivities(BaseFeed feed, Class<T> type, List<T> activities) throws StreamClientException, IOException {
		return streamActivityRepository.updateActivities(feed, type, activities);
	}

	@Override
	public <T extends BaseActivity> T addActivityToMany(List<String> targetIds, T activity) throws StreamClientException, IOException {
		return streamActivityRepository.addToMany(targetIds, activity);
	}

	@Override
	public <T extends BaseActivity> StreamResponse<AggregatedActivity<T>> getAggregatedActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		return streamActivityRepository.getAggregatedActivities(feed, type, filter);
	}

	@Override
	public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		return streamActivityRepository.getNotificationActivities(feed, type, filter);
	}

	@Override
	public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, boolean markAsRead, boolean markAsSeen) throws IOException, StreamClientException {
		return streamActivityRepository.getNotificationActivities(feed, type, filter, markAsRead, markAsSeen);
	}

	@Override
	public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException {
		return streamActivityRepository.getNotificationActivities(feed, type, filter, markAsRead, markAsSeen);
	}

	@Override
	public String getToken(BaseFeed feed) {
		return StreamRepoUtils.createFeedToken(feed, secretKey);
	}

	@Override
	public void shutdown() throws IOException {
		this.httpClient.close();
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
		return StreamRepoUtils.addAuthentication(feed, secretKey, httpRequest);
	}

	/**
	 * Get Jackson's object mapper. This method is deprecated.
	 * Please use StreamClientImpl.getObjectMapper() instead.
	 * @return Object mapper
     */
	@Deprecated
	public static ObjectMapper getObjectMapper() {
		return StreamClientImpl.getObjectMapper();
	}
}
