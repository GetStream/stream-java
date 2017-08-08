package io.getstream.client.apache.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.getstream.client.apache.StreamClientImpl;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.config.StreamRegion;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.beans.FollowMany;
import io.getstream.client.model.feeds.Feed;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class StreamRepositoryImplTest {

    public static final String API_KEY = "nfq26m3qgfyp";
    public static final String API_SECRET = "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    private final StreamRepositoryImpl streamRepository;

    private final StreamClientImpl streamClient;

    public StreamRepositoryImplTest() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setRegion(StreamRegion.LOCAL_TEST);
        streamClient = new StreamClientImpl(clientConfiguration, API_KEY, API_SECRET);
        streamRepository = new StreamRepositoryImpl(mock(ObjectMapper.class), clientConfiguration, mock(CloseableHttpClient.class));
    }

    @Test
    public void shouldNotFailWithExtraField() throws IOException {
        String toParse = "{\"actor\":null,\"verb\":null,\"object\":null,\"target\":null,\"time\":null," +
                "\"to\":[],\"foreign_id\":null,\"my_extra_field\":null}";

        ObjectMapper objectMapper = streamRepository.getObjectMapper();
        SimpleActivity simpleActivity = objectMapper.readValue(toParse, SimpleActivity.class);
    }

    @Test
    public void shouldParseLowerCaseWithUnderscore() throws IOException {
        String toParse = "{\"actor\":null,\"verb\":null,\"object\":null,\"target\":null,\"time\":null," +
                "\"to\":[],\"foreign_id\":\"id\"}";

        ObjectMapper objectMapper = streamRepository.getObjectMapper();
        SimpleActivity simpleActivity = objectMapper.readValue(toParse, SimpleActivity.class);
        assertThat(simpleActivity.getForeignId(), is("id"));
    }

    @Test
    public void shouldFollowMany() throws StreamClientException, IOException {
        stubFor(post(urlEqualTo("/api/v1.0/follow_many/?activity_copy_limit=300"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"duration\": \"7ms\"}")));

        Feed feed = streamClient.newFeed("user", "0");
        FollowMany followMany = new FollowMany.Builder()
                .add("user:0", "user:1")
                .add("user:0", "user:2")
                .add("user:0", "user:3")
                .build();
        feed.followMany(followMany);

        verify(postRequestedFor(urlEqualTo("/api/v1.0/follow_many/?activity_copy_limit=300"))
                .withHeader("X-Api-Key", equalTo("nfq26m3qgfyp"))
                .withHeader("Date", matching(".*"))
                .withHeader("Authorization", containing("Signature keyId=\"nfq26m3qgfyp\",algorithm=\"hmac-sha256\",headers=\"date\""))
                .withRequestBody(containing("\"target\":\"user:1\""))
                .withRequestBody(containing("\"target\":\"user:2\""))
                .withRequestBody(containing("\"target\":\"user:3\""))
        );
    }
}