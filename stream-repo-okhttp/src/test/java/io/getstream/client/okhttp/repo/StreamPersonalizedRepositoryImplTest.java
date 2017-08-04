package io.getstream.client.okhttp.repo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.squareup.okhttp.OkHttpClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.config.StreamRegion;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.PersonalizedActivity;
import io.getstream.client.model.feeds.PersonalizedFeed;
import io.getstream.client.okhttp.StreamClientImpl;
import io.getstream.client.util.JwtAuthenticationUtil;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class StreamPersonalizedRepositoryImplTest {

    public static final String API_KEY = "nfq26m3qgfyp";
    public static final String API_SECRET = "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    private final StreamPersonalizedRepositoryImpl streamRepository;

    private final StreamClientImpl streamClient;

    public StreamPersonalizedRepositoryImplTest() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setRegion(StreamRegion.LOCAL_TEST);
        clientConfiguration.setPersonalizedFeedEndpoint("http://localhost:8089/yourcompany/");
        streamClient = new StreamClientImpl(clientConfiguration, API_KEY, API_SECRET);
        streamRepository = new StreamPersonalizedRepositoryImpl(mock(ObjectMapper.class), clientConfiguration, mock(OkHttpClient.class));
    }

    @Test
    public void shouldGet() throws StreamClientException, IOException {
        stubFor(get(urlEqualTo("/yourcompany/personalized_feed/1234/?api_key=" + API_KEY))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"duration\": \"7ms\", \"results\":[{\"tags\":[\"foo\", \"bar\"]}]}")));

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("user", "1234");
        List<PersonalizedActivity> personalizedActivities = feed.get(PersonalizedActivity.class, null);

        System.out.println(JwtAuthenticationUtil.generateToken(API_SECRET, "*", "*", null, "1234"));
        verify(getRequestedFor(urlEqualTo("/yourcompany/personalized_feed/1234/?api_key=" + API_KEY))
                .withHeader("stream-auth-type", equalTo("jwt"))
                .withHeader("Authorization", containing(JwtAuthenticationUtil.generateToken(API_SECRET, "*", "*", null, "1234"))));
        assertThat(personalizedActivities.get(0).getTags(), (Matcher) hasItems("foo", "bar"));
    }

    @Test
    public void shouldSendMeta() throws StreamClientException, IOException {
        stubFor(post(urlEqualTo("/yourcompany/meta/?api_key=" + API_KEY))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"duration\": \"0.07\"}")));

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("user", "1234");
        feed.addMeta(feed, new MetaInterest(Collections.singletonList("baseball")));

        verify(postRequestedFor(urlEqualTo("/yourcompany/meta/?api_key=" + API_KEY))
                .withHeader("stream-auth-type", equalTo("jwt"))
                .withHeader("Authorization", containing(JwtAuthenticationUtil.generateToken(API_SECRET, "*", "*", null, "1234")))
                .withRequestBody(containing("{\"data\":{\"user:1234\":{\"interests\":[\"baseball\"]}}}")));
    }

    class MetaInterest implements Serializable {

        private List<String> interests = null;

        public MetaInterest() {
        }

        public MetaInterest(List<String> interests) {
            this.interests = interests;
        }

        public List<String> getInterests() {
            return interests;
        }

        public void setInterests(List<String> interests) {
            this.interests = interests;
        }
    }

    class Interest implements Serializable {

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("consumption_history")
        private List<String> consumptionHistory;

        @JsonProperty("profile_strength")
        private Card profileStrength;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public List<String> getConsumptionHistory() {
            return consumptionHistory;
        }

        public void setConsumptionHistory(List<String> consumptionHistory) {
            this.consumptionHistory = consumptionHistory;
        }

        public Card getProfileStrength() {
            return profileStrength;
        }

        public void setProfileStrength(Card profileStrength) {
            this.profileStrength = profileStrength;
        }
    }

    class Card implements Serializable {
        private int card;

        public int getCard() {
            return card;
        }

        public void setCard(int card) {
            this.card = card;
        }
    }
}