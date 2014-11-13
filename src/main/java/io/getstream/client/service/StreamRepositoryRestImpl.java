package io.getstream.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.BaseActivity;
import io.getstream.client.model.BaseFeed;
import io.getstream.client.model.Feed;
import io.getstream.client.model.SimpleActivity;
import io.getstream.client.utils.SignatureUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public class StreamRepositoryRestImpl implements StreamRepository {

    private static final String API_KEY = "api_key";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final URI baseEndpoint;
    private final String apiKey;
	private final String secretKey;

	private CloseableHttpClient httpClient;

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

    @Override
    public <T extends BaseActivity> void addActivity(BaseFeed feed, T activity) throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        HttpPost httpPost = new HttpPost(UriBuilder.fromUri(baseEndpoint).path("feed").path("{feedSlug}").path("{userId}")
                                                 .queryParam(API_KEY, apiKey).build(feed.getFeedSlug(), feed.getUserId()));
        String tokenId = feed.getFeedSlug().concat(feed.getUserId());
        httpPost.addHeader("Authorization", String.format("%s %s", tokenId, SignatureUtils.calculateHMAC(secretKey, tokenId)));

        httpPost.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(activity)));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //handle response
            }
        } finally {
            response.close();
        }
    }

    public List<SimpleActivity> getActivities(String id) throws IOException {
        HttpGet httpGet = new HttpGet(UriBuilder.fromUri(baseEndpoint).path("feed")
                                              .path("{type}").path("{userId}").queryParam(API_KEY, apiKey)
                                              .build("flat", id));
		try {
			httpGet.addHeader("Authorization", String.format("%s %s", id, SignatureUtils.calculateHMAC(secretKey, id)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //return OBJECT_MAPPER.readValue(entity.getContent(), feetType.getClassType());
            }
        } finally {
            response.close();
        }
        return null;
    }

}
