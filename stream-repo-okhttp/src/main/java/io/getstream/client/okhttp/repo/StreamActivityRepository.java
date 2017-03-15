package io.getstream.client.okhttp.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
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
import io.getstream.client.okhttp.repo.handlers.StreamExceptionHandler;
import io.getstream.client.okhttp.repo.utils.FeedFilterUtils;
import io.getstream.client.okhttp.repo.utils.SignatureUtils;
import io.getstream.client.okhttp.repo.utils.StreamRepoUtils;
import io.getstream.client.okhttp.repo.utils.UriBuilder;
import io.getstream.client.util.HttpSignatureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static io.getstream.client.util.JwtAuthenticationUtil.ALL;
import static io.getstream.client.util.JwtAuthenticationUtil.generateToken;

public class StreamActivityRepository {

    private static final Logger LOG = LoggerFactory.getLogger(StreamActivityRepository.class);

	private static final String APPLICATION_JSON = "application/json; charset=utf-8";

	private final ObjectMapper objectMapper;
	private final URI baseEndpoint;
	private final String apiKey;
	private final StreamExceptionHandler exceptionHandler;
	private final OkHttpClient httpClient;
	private final String secretKey;

	public StreamActivityRepository(ObjectMapper objectMapper, URI baseEndpoint, String apiKey,
                                    StreamExceptionHandler exceptionHandler, OkHttpClient httpClient, String secretKey) {
        this.objectMapper = objectMapper;
        this.baseEndpoint = baseEndpoint;
        this.apiKey = apiKey;
        this.exceptionHandler = exceptionHandler;
        this.httpClient = httpClient;
        this.secretKey = secretKey;
    }

	public <T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException {
		Request.Builder requestBuilder = new Request.Builder().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey).build().toURL());

		SignatureUtils.addSignatureToRecipients(secretKey, activity);
		requestBuilder.post(RequestBody.create(MediaType.parse(APPLICATION_JSON), objectMapper.writeValueAsString(activity)));

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructType(activity.getClass()));
	}

	public <T extends BaseActivity> StreamActivitiesResponse<T> addActivities(BaseFeed feed, Class<T> type, List<T> activities) throws StreamClientException, IOException {
		Request.Builder requestBuilder = new Request.Builder().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey).build().toURL());

		for (T activity : activities) {
			SignatureUtils.addSignatureToRecipients(secretKey, activity);
		}

		requestBuilder.post(RequestBody.create(MediaType.parse(APPLICATION_JSON),
				objectMapper.writeValueAsString(Collections.singletonMap("activities", activities))));

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructParametricType(StreamActivitiesResponse.class, type));
	}

	public <T extends BaseActivity> T addToMany(List<String> targetIds, T activity) throws StreamClientException, IOException {
		Request.Builder requestBuilder = new Request.Builder().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed")
				.path("add_to_many/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey).build().toURL());

		requestBuilder.addHeader(HttpSignatureHandler.X_API_KEY_HEADER, apiKey);
		requestBuilder.post(RequestBody.create(MediaType.parse(APPLICATION_JSON),
				objectMapper.writeValueAsString(new AddMany<>(targetIds, activity))));

		final Request request = requestBuilder.build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructType(activity.getClass()));
	}

	public <T extends BaseActivity> StreamResponse<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		Request.Builder requestBuilder = new Request.Builder().url(FeedFilterUtils.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey), filter).build().toURL()).get();

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, type));
	}

	public <T extends BaseActivity> StreamResponse<AggregatedActivity<T>> getAggregatedActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		Request.Builder requestBuilder = new Request.Builder().url(FeedFilterUtils.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey), filter).build().toURL()).get();

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, objectMapper.getTypeFactory().constructParametricType(AggregatedActivity.class, type)));
	}

	public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		Request.Builder requestBuilder = new Request.Builder().url(FeedFilterUtils.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey), filter).build().toURL()).get();

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructParametricType(StreamResponse.class,
						objectMapper.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
	}

	public <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, boolean markAsRead, boolean markAsSeen) throws IOException, StreamClientException {
		Request.Builder requestBuilder = new Request.Builder().url(FeedFilterUtils.apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId() + "/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey)
				.queryParam("mark_read", Boolean.toString(markAsRead))
				.queryParam("mark_seen", Boolean.toString(markAsSeen)), filter).build().toURL()).get();

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, objectMapper.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
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

		Request.Builder requestBuilder = new Request.Builder().url(FeedFilterUtils.apply(baseUri, filter).build().toURL()).get();

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructParametricType(StreamResponse.class, objectMapper.getTypeFactory().constructParametricType(NotificationActivity.class, type)));
	}

    public void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException {
        Request.Builder requestBuilder = new Request.Builder().delete().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path(activityId + "/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey).build().toURL());

		Request request = addAuthentication(feed, requestBuilder).build();
        LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
        handleResponseCode(response);
    }

	public void deleteActivityByForeignId(BaseFeed feed, String activityId) throws IOException, StreamClientException {
		Request.Builder requestBuilder = new Request.Builder().delete().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path(activityId + "/")
				.queryParam("foreign_id", Boolean.toString(true))
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey).build().toURL());

		Request request = addAuthentication(feed, requestBuilder).build();
		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
	}

	public <T extends BaseActivity> StreamActivitiesResponse<T> updateActivities(BaseFeed feed, Class<T> type, List<T> activities) throws IOException, StreamClientException {
		Request.Builder requestBuilder = new Request.Builder().delete().url(UriBuilder.fromEndpoint(baseEndpoint)
				.path("activities/")
				.queryParam(StreamRepositoryImpl.API_KEY, apiKey).build().toURL());

		requestBuilder.post(RequestBody.create(MediaType.parse(APPLICATION_JSON),
				objectMapper.writeValueAsBytes(Collections.singletonMap("activities", activities))));

		Request request = StreamRepoUtils.addJwtAuthentication(
				generateToken(secretKey, ALL, "activities", ALL, null),
				requestBuilder).build();

		LOG.debug("Invoking url: '{}", request.urlString());

		Response response = httpClient.newCall(request).execute();
		handleResponseCode(response);
		return objectMapper.readValue(response.body().byteStream(),
				objectMapper.getTypeFactory().constructParametricType(StreamActivitiesResponse.class, type));
	}

	private void handleResponseCode(Response response) throws StreamClientException, IOException {
		exceptionHandler.handleResponseCode(response);
	}

	private Request.Builder addAuthentication(BaseFeed feed, Request.Builder httpRequest) {
		return StreamRepoUtils.addAuthentication(feed, secretKey, httpRequest);
	}
}
