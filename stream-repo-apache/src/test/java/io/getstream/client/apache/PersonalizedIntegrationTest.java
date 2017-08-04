package io.getstream.client.apache;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.config.StreamRegion;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.PersonalizedActivity;
import io.getstream.client.model.beans.MetaResponse;
import io.getstream.client.model.feeds.PersonalizedFeed;
import io.getstream.client.model.filters.FeedFilter;
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

    public static final String PERSONALIZED_FEED_ENDPOINT = "";
    public static final String API_KEY = "";
    public static final String API_SECRET = "";
    public static final ClientConfiguration CLIENT_CONFIGURATION = new ClientConfiguration(StreamRegion.QA_TEST);

    @BeforeClass
    public static void setLog() {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
    }

    @BeforeClass
    public static void initClient() {
        CLIENT_CONFIGURATION.setPersonalizedFeedEndpoint(PERSONALIZED_FEED_ENDPOINT);
    }

    @Test
    public void shouldGetPersonalizedFeed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY, API_SECRET);

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("aggregated", "f4774599-a0e5-46cb-81e8-451cb38d3fc7");
        List<PersonalizedActivity> response = feed.get(PersonalizedActivity.class, new FeedFilter.Builder().withLimit(20).build());
        assertTrue(response.size() > 0);
        for (PersonalizedActivity personalizedActivity : response) {
            System.out.println(personalizedActivity.getId());
            System.out.println(personalizedActivity.getTime());
        }


        streamClient.shutdown();
    }

    @Test
    public void shouldGetPersonalizedFeedInterest() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY, API_SECRET);

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("aggregated", "f4774599-a0e5-46cb-81e8-451cb38d3fc7");
        List<Interest> response = feed.getInterest(Interest.class);
        assertTrue(response.size() > 0);

        streamClient.shutdown();
    }


    @Test
    public void shouldSendMeta() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY, API_SECRET);

        PersonalizedFeed feed = streamClient.newPersonalizedFeed("user", "f4774599-a0e5-46cb-81e8-451cb38d3fc7");

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