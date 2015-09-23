package io.getstream.client.apache.example.mixtype;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.getstream.client.apache.StreamClientImpl;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.service.FlatActivityServiceImpl;

import java.io.IOException;
import java.util.Arrays;

/**
 * The following example creates a new custom activity and push it into the feed.
 */
public class MixedType {

    public static void main(String[] args) throws IOException, StreamClientException {
        /**
         * Enable wire logging programmatically.
         */
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");

        /**
         * Create client using api key and secret key.
         */
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), "nfq26m3qgfyp",
                                                                "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");

        /**
         * Get the reference to a feed (either new or existing one).
         */
        Feed feed = streamClient.newFeed("user", "9");

        FlatActivityServiceImpl<Match> matchActivityService = feed.newFlatActivityService(Match.class);

        VolleyballMatch volley = new VolleyballMatch();
        volley.setActor("Me");
        volley.setObject("Message");
        volley.setTarget("");
        volley.setTo(Arrays.asList("user:1"));
        volley.setVerb("verb");

        volley.setNrOfBlocked(1);
        volley.setNrOfServed(1);
        matchActivityService.addActivity(volley);

        FootballMatch football = new FootballMatch();
        football.setActor("Me");
        football.setObject("Message");
        football.setTarget("");
        football.setTo(Arrays.asList("user:1"));
        football.setVerb("verb");

        football.setNrOfPenalty(2);
        football.setNrOfScore(3);
        matchActivityService.addActivity(football);

        /**
         * Try to retrieve a mixed type of activities.
         */
        for (Match match : matchActivityService.getActivities().getResults()) {
            System.out.println(match);
            feed.deleteActivity(match.getId());
        }

        streamClient.shutdown();
    }

    /**
     * This is the abstract class of the super-type Match.
     * It uses Jackson annotation to handle serialization/deserialization of subtypes.
     * Below annotations tells Jackson to use the field 'type' to figure out which subclass
     * to use.
     * In case of serialization Jackson will add a field 'type' into the json object adding
     * 'volley' or 'football' according to the instance type of the input bean.
     * In case of deserialization Jackson will read the field 'type' in order to figure out
     * which subtype of 'Match' class to instantiate.
     * 'JsonSubTypes' annotation is optional but make your json object more readable.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
                          @JsonSubTypes.Type(value = VolleyballMatch.class, name = "volley"),
                          @JsonSubTypes.Type(value = FootballMatch.class, name = "football")
    })
    static abstract class Match extends BaseActivity {
    }

    static class VolleyballMatch extends Match {
        private int nrOfServed;
        private int nrOfBlocked;

        public int getNrOfServed() {
            return nrOfServed;
        }

        public void setNrOfServed(int nrOfServed) {
            this.nrOfServed = nrOfServed;
        }

        public void setNrOfBlocked(int nrOfBlocked) {
            this.nrOfBlocked = nrOfBlocked;
        }

        public int getNrOfBlocked() {
            return nrOfBlocked;
        }
    }

    static class FootballMatch extends Match {
        private int nrOfPenalty;
        private int nrOfScore;

        public int getNrOfPenalty() {
            return nrOfPenalty;
        }

        public void setNrOfPenalty(int nrOfPenalty) {
            this.nrOfPenalty = nrOfPenalty;
        }

        public int getNrOfScore() {
            return nrOfScore;
        }

        public void setNrOfScore(int nrOfScore) {
            this.nrOfScore = nrOfScore;
        }
    }
}
