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
package io.getstream.client.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.handlers.StreamExceptionHandler;
import io.getstream.client.repo.utils.StreamRepoUtils;
import io.getstream.client.repo.utils.UriBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static io.getstream.client.repo.utils.FeedFilterUtils.apply;

/**
 * Actual implementation of the Stream's REST API calls.
 */
public class StreamRepositoryImpl implements StreamRepository {

	private static final Logger LOG = LoggerFactory.getLogger(StreamRepositoryImpl.class);

	static final String API_KEY = "api_key";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setPropertyNamingStrategy(
                                                                                                          /* will convert camelStyle to lower_case_style */
                                                                                                          PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

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
	 * @param streamClient
	 * @param closeableHttpClient
	 */
	public StreamRepositoryImpl(ClientConfiguration streamClient, CloseableHttpClient closeableHttpClient) {
		this.baseEndpoint = streamClient.getRegion().getEndpoint();
		this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
		this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
		this.exceptionHandler = new StreamExceptionHandler(OBJECT_MAPPER);
		this.httpClient = closeableHttpClient;
		this.streamActivityRepository = new StreamActivityRepository(OBJECT_MAPPER, baseEndpoint, apiKey, exceptionHandler,
				httpClient, secretKey);
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
		HttpGet request = new HttpGet(apply(UriBuilder.fromEndpoint(baseEndpoint)
				.path("feed").path(feed.getFeedSlug()).path(feed.getUserId()).path("following/")
				.queryParam(API_KEY, apiKey), filter).build());
		LOG.debug("Invoking url: '{}'", request.getURI());
		try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
			handleResponseCode(response);
			StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
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
			StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
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

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}
}
