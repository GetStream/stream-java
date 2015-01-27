package io.getstream.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.bean.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.feeds.FeedFollow;
import io.getstream.client.utils.SignatureUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Collections;
import java.util.List;

public class StreamRepositoryRestImpl {

    private final static Logger LOG = LoggerFactory.getLogger(StreamRepositoryRestImpl.class);

    private static final String API_KEY = "api_key";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final URI baseEndpoint;
    private final String apiKey;
	private final String secretKey;

	private CloseableHttpClient httpClient;

    private static final int DEFAULT_LIMIT = 25;

    public StreamRepositoryRestImpl(ClientConfiguration streamClient) {
        try {
            this.baseEndpoint = new URI(StreamClient.BASE_ENDPOINT);
            this.apiKey = streamClient.getAuthenticationHandlerConfiguration().getApiKey();
            this.secretKey = streamClient.getAuthenticationHandlerConfiguration().getSecretKey();
			initialiseClient(streamClient);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot initialise client. Wrong getStream.io base endpoint.");
        }
	}

	private void initialiseClient(final ClientConfiguration streamClient) {
		this.httpClient = HttpClients.custom().build();
	}

    public <T extends BaseActivity> void addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException {
		HttpPost request = new HttpPost(UriBuilder.fromUri(baseEndpoint)
				.path("feed").path("{feedSlug}").path("{userId}/")
				.queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));
		LOG.debug("Invoking url: '{}'", request.getURI());
		request.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(activity), ContentType.APPLICATION_JSON));
		try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
			if (checkResponseStatus(response) != 201) {
				throw new StreamClientException("Error creating activity");
			}
		}
	}

    public <T extends BaseActivity> List<T> getActivities(BaseFeed feed) throws IOException, StreamClientException {
        HttpGet request = new HttpGet(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}/")
                                              .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
            HttpEntity entity = response.getEntity();
            if (checkResponseStatus(response) != 200) {
                throw new StreamClientException("Error retrieving activity");
            }
			StreamResponse<T> streamResponse = OBJECT_MAPPER.readValue(entity.getContent(),
					new TypeReference<StreamResponse<T>>(){});
			return streamResponse.getResults();
        }
    }

    public void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException {
        HttpDelete request = new HttpDelete(UriBuilder.fromUri(baseEndpoint)
                                              .path("feed").path("{feedSlug}").path("{userId}").path("{id}")
                                              .queryParam(API_KEY, apiKey)
                                              .build(feed.getFeedSlug(), feed.getUserId(), activityId));
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
            HttpEntity entity = response.getEntity();
            if (checkResponseStatus(response) != 200) {
                throw new StreamClientException("Error delete activity");
            }
        }
    }

    public void follow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException {
        HttpPost request = new HttpPost(UriBuilder.fromUri(baseEndpoint)
                                              .path("feed").path("{feedSlug}").path("{userId}").path("following/")
                                              .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));

        request.setEntity(new UrlEncodedFormEntity(
                                  Collections.singletonList(new BasicNameValuePair("target", targetFeedId))));

        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
            if (checkResponseStatus(response) != 201) {
                throw new StreamClientException("Error follow target: " + targetFeedId);
            }
        }
    }

    public void unfollow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException {
        HttpDelete request = new HttpDelete(UriBuilder.fromUri(baseEndpoint)
                                                .path("feed").path("{feedSlug}").path("{userId}").path("following").path("{target}/")
                                                .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId(), targetFeedId));
        LOG.debug("Invoking url: '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
            if (checkResponseStatus(response) != 200) {
                throw new StreamClientException("Error unfollowing the feed: " + targetFeedId);
            }
        }
    }

    public List<FeedFollow> getFollowing(BaseFeed feed) throws StreamClientException, IOException {
        HttpGet request = new HttpGet(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}").path("following/")
                                              .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));
        LOG.debug("Invoking the following url '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
            HttpEntity entity = response.getEntity();
            if (checkResponseStatus(response) != 200) {
                throw new StreamClientException("Error retrieving following");
            }
            StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(entity.getContent(),
                                                   new TypeReference<StreamResponse<FeedFollow>>(){});
            return streamResponse.getResults();
        }
    }

    public List<FeedFollow> getFollowers(BaseFeed feed) throws StreamClientException, IOException {
        HttpGet request = new HttpGet(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}").path("followers/")
                                              .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));
        LOG.debug("Invoking the followers url '{}'", request.getURI());
        try (CloseableHttpResponse response = httpClient.execute(addAuthentication(feed, request))) {
            HttpEntity entity = response.getEntity();
            if (checkResponseStatus(response) != 200) {
                throw new StreamClientException("Error retrieving followers");
            }
            StreamResponse<FeedFollow> streamResponse = OBJECT_MAPPER.readValue(entity.getContent(),
                                                    new TypeReference<StreamResponse<FeedFollow>>(){});
            return streamResponse.getResults();
        }
    }

    private int checkResponseStatus(CloseableHttpResponse response) throws IOException {
        final int responseCode = response.getStatusLine().getStatusCode();
		if (responseCode != 200) {
			LOG.debug("Response code: {}", responseCode);
			LOG.debug(EntityUtils.toString(response.getEntity(), "UTF-8"));
		}
        return responseCode;
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
