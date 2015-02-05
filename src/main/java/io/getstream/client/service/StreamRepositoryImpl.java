package io.getstream.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.model.bean.FeedFollow;
import io.getstream.client.model.bean.StreamResponse;
import io.getstream.client.utils.SignatureUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
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

public class StreamRepositoryImpl implements StreamRepository {

    private final static Logger LOG = LoggerFactory.getLogger(StreamRepositoryImpl.class);

    private static final String API_KEY = "api_key";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final URI baseEndpoint;
    private final String apiKey;
	private final String secretKey;

	private CloseableHttpClient httpClient;

	public StreamRepositoryImpl(ClientConfiguration streamClient) {
		this.baseEndpoint = streamClient.getRegion().getEndpoint();
		this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
		this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
		this.httpClient = HttpClients.custom().build();
	}

    @Override
	public <T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException {
		HttpPost request = new HttpPost(UriBuilder.fromUri(baseEndpoint)
				.path("feed").path("{feedSlug}").path("{userId}/")
				.queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));

		addSignatureToRecipients(secretKey, activity);

		request.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(activity), APPLICATION_JSON));
		try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
			handleResponseCode(response.getStatusLine().getStatusCode());
			return OBJECT_MAPPER.readValue(response.getEntity().getContent(),
					OBJECT_MAPPER.getTypeFactory().constructType(activity.getClass()));
		}
	}

	@Override
	public <T extends BaseActivity> List<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
		HttpGet request = new HttpGet(filter.apply(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}/")
				.queryParam(API_KEY, apiKey)).build(feed.getFeedSlug(), feed.getUserId()));

		try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
			handleResponseCode(response.getStatusLine().getStatusCode());
			StreamResponse<T> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
					OBJECT_MAPPER.getTypeFactory().constructParametricType(StreamResponse.class, type));
			return streamResponse.getResults();
		}
	}

	@Override
	public void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException {
        HttpDelete request = new HttpDelete(UriBuilder.fromUri(baseEndpoint)
                                              .path("feed").path("{feedSlug}").path("{userId}").path("{id}")
                                              .queryParam(API_KEY, apiKey)
                                              .build(feed.getFeedSlug(), feed.getUserId(), activityId));
		fireAndForget(addAuthentication(feed, request));
    }

    @Override
	public void follow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException {
        HttpPost request = new HttpPost(UriBuilder.fromUri(baseEndpoint)
                                              .path("feed").path("{feedSlug}").path("{userId}").path("following/")
                                              .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));

        request.setEntity(new UrlEncodedFormEntity(
                                  Collections.singletonList(new BasicNameValuePair("target", targetFeedId))));
		fireAndForget(addAuthentication(feed, request));
    }

    @Override
	public void unfollow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException {
        HttpDelete request = new HttpDelete(UriBuilder.fromUri(baseEndpoint)
                                                .path("feed").path("{feedSlug}").path("{userId}").path("following").path("{target}/")
                                                .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId(), targetFeedId));
		fireAndForget(addAuthentication(feed, request));
    }

    @Override
	public List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
        HttpGet request = new HttpGet(filter.apply(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}").path("following/")
                                              .queryParam(API_KEY, apiKey)).build(feed.getFeedSlug(), feed.getUserId()));
        LOG.debug("Invoking the following url '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
			handleResponseCode(response.getStatusLine().getStatusCode());
            StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
                                                   new TypeReference<StreamResponse<FeedFollow>>(){});
            return streamResponse.getResults();
        }
    }

    @Override
	public List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException {
        HttpGet request = new HttpGet(filter.apply(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}").path("followers/")
                                              .queryParam(API_KEY, apiKey)).build(feed.getFeedSlug(), feed.getUserId()));
        LOG.debug("Invoking the followers url '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
			handleResponseCode(response.getStatusLine().getStatusCode());
            StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(response.getEntity().getContent(),
					new TypeReference<StreamResponse<FeedFollow>>() {
					});
            return streamResponse.getResults();
        }
	}

	public CloseableHttpResponse getActivitiesStream(BaseFeed feed, FeedFilter filter) throws IOException, StreamClientException {
		HttpGet request = new HttpGet(filter.apply(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}/")
				.queryParam(API_KEY, apiKey)).build(feed.getFeedSlug(), feed.getUserId()));
		return httpClient.execute(addAuthentication(feed, request));
	}

	private void fireAndForget(final HttpRequestBase request) throws IOException, StreamClientException {
		LOG.debug("Invoking url: '{}", request.getURI());
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			handleResponseCode(response.getStatusLine().getStatusCode());
		}
	}

	private void handleResponseCode(final int responseCode) throws StreamClientException {
		switch (responseCode) {
			case 400:
				throw new StreamClientException("Error delete activity");
			case 401:
				throw new StreamClientException("Error delete activity");
			case 404:
				throw new StreamClientException();
			case 500:
				throw new StreamClientException("Error delete activity");
		}
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
}
