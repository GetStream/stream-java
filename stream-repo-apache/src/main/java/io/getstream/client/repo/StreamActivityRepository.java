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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.handlers.StreamExceptionHandler;
import io.getstream.client.repo.utils.SignatureUtils;
import io.getstream.client.repo.utils.StreamRepoUtils;
import io.getstream.client.repo.utils.UriBuilder;
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

import static io.getstream.client.repo.utils.FeedFilterUtils.apply;
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

        request.setEntity(new StringEntity(objectMapper.writeValueAsString(activity), APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request), HttpClientContext.create())) {
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

    private HttpRequestBase addAuthentication(BaseFeed feed, HttpRequestBase request) {
        return StreamRepoUtils.addAuthentication(feed, secretKey, request);
    }

    private void handleResponseCode(CloseableHttpResponse response) throws StreamClientException, IOException {
        exceptionHandler.handleResponseCode(response);
    }
}
