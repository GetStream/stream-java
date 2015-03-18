/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package client.repo;

import client.repo.handlers.StreamExceptionHandler;
import client.repo.utils.SignatureUtils;
import client.repo.utils.StreamRepoUtils;
import client.repo.utils.UriBuilder;
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
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import static client.repo.utils.FeedFilterUtils.apply;

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

	public <T extends BaseActivity> StreamResponse<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		Request.Builder requestBuilder = new Request.Builder().url(apply(UriBuilder.fromEndpoint(baseEndpoint)
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
		Request.Builder requestBuilder = new Request.Builder().url(apply(UriBuilder.fromEndpoint(baseEndpoint)
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
		Request.Builder requestBuilder = new Request.Builder().url(apply(UriBuilder.fromEndpoint(baseEndpoint)
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
		Request.Builder requestBuilder = new Request.Builder().url(apply(UriBuilder.fromEndpoint(baseEndpoint)
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

		Request.Builder requestBuilder = new Request.Builder().url(apply(baseUri, filter).build().toURL()).get();

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

	private void handleResponseCode(Response response) throws StreamClientException, IOException {
		exceptionHandler.handleResponseCode(response);
	}

	private Request.Builder addAuthentication(BaseFeed feed, Request.Builder httpRequest) {
		return StreamRepoUtils.addAuthentication(feed, secretKey, httpRequest);
	}
}
