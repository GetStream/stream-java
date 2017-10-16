package io.getstream.client.okhttp.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
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
import io.getstream.client.okhttp.StreamClientImpl;
import io.getstream.client.okhttp.repo.handlers.StreamExceptionHandler;
import io.getstream.client.okhttp.repo.utils.FeedFilterUtils;
import io.getstream.client.okhttp.repo.utils.StreamRepoUtils;
import io.getstream.client.okhttp.repo.utils.UriBuilder;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.util.EndpointUtil;
import io.getstream.client.util.HttpSignatureHandler;
import io.getstream.client.util.JwtAuthenticationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Actual implementation of the Stream's REST API calls.
 */
public class StreamRepositoryImpl implements StreamRepository {

	private static final String APPLICATION_JSON = "application/json; charset=utf-8";

	private static final Logger LOG = LoggerFactory.getLogger(StreamRepositoryImpl.class);

	static final String API_KEY = "api_key";

	private final ObjectMapper objectMapper;
	private final URI baseEndpoint;
	private final String apiKey;
	private final String secretKey;
	private final StreamExceptionHandler exceptionHandler;

	private final OkHttpClient httpClient;
	private final StreamActivityRepository streamActivityRepository;

	/**
	 * Create a new {@link StreamRepository} using the given configuration {@link ClientConfiguration} and
	 * a pre-instantiated HttpClient {@link OkHttpClient}.
	 *
	 * @param objectMapper Json Object Mapper
	 * @param streamClient Client configuration
	 * @param closeableHttpClient Actual instance of OkHTTP client
	 */
	public StreamRepositoryImpl(ObjectMapper objectMapper, ClientConfiguration streamClient, OkHttpClient closeableHttpClient) {
		this.baseEndpoint = EndpointUtil.getBaseEndpoint(streamClient);
		this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
		this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
		this.exceptionHandler = new StreamExceptionHandler(objectMapper);
		this.httpClient = closeableHttpClient;
		this.objectMapper = objectMapper;
		this.streamActivityRepository = new StreamActivityRepository(objectMapper, baseEndpoint, apiKey, exceptionHandler,
				httpClient, secretKey);
	}

	@Override
	public String getReadOnlyToken(BaseFeed feed) {
		return JwtAuthenticationUtil.generateToken(secretKey, "read", "*", feed.getFeedSlug().concat(feed.getUserId()), null);
	}

	@Override
	public String getToken(BaseFeed feed) {
		return StreamRepoUtils.createFeedToken(feed, secretKey);
	}

	@Override
	public void follow(BaseFeed feed, String targetFeedId, int activityCopyLimit) throws StreamClientException, IOException {
		Request.Builder requestBuilder = new Request.Builder().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following/")
				.queryParam(API_KEY, apiKey).build().toURL());
		requestBuilder.post(
				RequestBody.create(MediaType.parse(APPLICATION_JSON),
						objectMapper.writeValueAsString(new FollowRequest(targetFeedId, activityCopyLimit))));

		Request request = addAuthentication(feed, requestBuilder).build();
		fireAndForget(request);
	}

	@Override
	public void followMany(BaseFeed feed, FollowMany followManyInput, int activityCopyLimit) throws StreamClientException,
			IOException {
		Request.Builder requestBuilder = new Request.Builder().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("follow_many/")
				.queryParam("activity_copy_limit", activityCopyLimit)
				.build().toURL());
		requestBuilder.addHeader(HttpSignatureHandler.X_API_KEY_HEADER, apiKey);
		requestBuilder.post(RequestBody.create(MediaType.parse(APPLICATION_JSON), objectMapper.writeValueAsString(followManyInput)));
		fireAndForget(requestBuilder.build());
	}

	@Override
	public void unfollow(BaseFeed feed, String targetFeedId, boolean keepHistory) throws StreamClientException, IOException {
		Request.Builder requestBuilder = new Request.Builder().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following").path(targetFeedId + "/")
				.queryParam("keep_history", Boolean.toString(keepHistory))
				.queryParam(API_KEY, apiKey).build().toURL()).delete();
		fireAndForget(addAuthentication(feed, requestBuilder).build());
	}

	@Override
	public List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
		Request.Builder requestBuilder = new Request.Builder().url(FeedFilterUtils.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following/")
				.queryParam(API_KEY, apiKey), filter).build().toURL()).get();

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}'", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		StreamResponse<FeedFollow> streamResponse = objectMapper.readValue(response.body().byteStream(),
				new TypeReference<StreamResponse<FeedFollow>>() {});
		return streamResponse.getResults();
	}

	@Override
	public List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
		Request.Builder requestBuilder = new Request.Builder().url(FeedFilterUtils.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("followers/")
				.queryParam(API_KEY, apiKey), filter).build().toURL()).get();

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}'", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		StreamResponse<FeedFollow> streamResponse = objectMapper.readValue(response.body().byteStream(),
				new TypeReference<StreamResponse<FeedFollow>>() {});
		return streamResponse.getResults();
	}

	@Override
	public void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException {
		streamActivityRepository.deleteActivityById(feed, activityId);
	}

	@Override
	public void deleteActivityByForeignId(BaseFeed feed, String activityId) throws IOException, StreamClientException {
		streamActivityRepository.deleteActivityByForeignId(feed, activityId);
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
	public <T extends BaseActivity> StreamActivitiesResponse<T> addActivities(BaseFeed feed, Class<T> type, List<T> activities) throws IOException, StreamClientException {
		return streamActivityRepository.addActivities(feed, type, activities);
	}

	@Override
	public <T extends BaseActivity> StreamActivitiesResponse<T> updateActivities(BaseFeed feed, Class<T> type, List<T> activities) throws IOException, StreamClientException {
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
	public void shutdown() throws IOException {
		//do nothing
	}

	private void fireAndForget(final Request request) throws IOException, StreamClientException {
		LOG.debug("Invoking url: '{}", request.urlString());
		handleResponseCode(httpClient.newCall(request).execute());
	}

	private void handleResponseCode(Response response) throws StreamClientException, IOException {
		exceptionHandler.handleResponseCode(response);
	}

	private Request.Builder addAuthentication(BaseFeed feed, Request.Builder httpRequest) {
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