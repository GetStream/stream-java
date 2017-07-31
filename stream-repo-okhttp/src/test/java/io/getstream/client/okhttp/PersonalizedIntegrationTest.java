package io.getstream.client.okhttp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.config.StreamRegion;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.PersonalizedActivity;
import io.getstream.client.model.beans.MetaResponse;
import io.getstream.client.model.feeds.PersonalizedFeed;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class PersonalizedIntegrationTest {

    public static final String API_KEY = "qssg44g3xjqu";
    public static final String API_SECRET = "kq3ueg2wpxz9qjqkuxgfmhn8p768mxygg4tqtvxymnbbrfsdktf3ntz8ndg9eg53";
    public static final ClientConfiguration CLIENT_CONFIGURATION = new ClientConfiguration(StreamRegion.QA_TEST);

    @BeforeClass
    public static void initClient() {
        CLIENT_CONFIGURATION.setPersonalizedFeedEndpoint("http://ml-api.staging.gtstrm.com:85/fullmeasureed");
    }

    @Test
    public void shouldGetPersonalizedFeed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY, API_SECRET);

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("aggregated", "2");
        List<PersonalizedActivity> response = feed.get(PersonalizedActivity.class);
        assertTrue(response.size() > 0);

        streamClient.shutdown();
    }

    @Test
    public void shouldGetPersonalizedFeedInterest() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY, API_SECRET);

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("aggregated", "2");
        List<Interest> response = feed.getInterest(Interest.class);
        assertTrue(response.size() > 0);

        streamClient.shutdown();
    }


    @Test
    public void shouldSendMeta() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY, API_SECRET);

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("user", "2");

        MetaResponse response = feed.addMeta(feed, new MetaInterest(Collections.singletonList("java")));
        assertThat(response.getResponseCode(), is(201));

        streamClient.shutdown();
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