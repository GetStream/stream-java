package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.utils.Enrichment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CloudFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";
    private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e1";
    private static final Token token = buildToken();
    private static String actorID;

    private static Token buildToken() {
        try {
            return Client.builder(apiKey, secret).build().frontendToken(userID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @BeforeAll
    static void setup() {
        try {
            actorID = Enrichment.createUserReference(Client.builder(apiKey, secret)
                    .build()
                    .user(userID)
                    .getOrCreate()
                    .join()
                    .getID());
        } catch (StreamException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addActivity() {
        Activity[] result = new Activity[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            Activity activity = Activity.builder()
                    .actor(actorID)
                    .verb("test")
                    .object("test")
                    .build();
            CloudFlatFeed feed = client.flatFeed("flat", userID);
            result[0] = feed.addActivity(activity).join();
        });
    }

    @Test
    void addActivities() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            Activity activity = Activity.builder()
                    .actor(actorID)
                    .verb("test")
                    .object("test")
                    .build();
            CloudFlatFeed feed = client.flatFeed("flat", userID);
            result[0] = feed.addActivities(activity).join();
        });
    }

    @Test
    void addCustomActivities() {
        List<Match>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            VolleyballMatch volley = new VolleyballMatch();
            volley.actor = actorID;
            volley.object = "Message";
            volley.verb = "verb";
            volley.setNrOfBlocked(1);
            volley.setNrOfServed(1);

            FootballMatch football = new FootballMatch();
            football.actor = actorID;
            football.object = "Message";
            football.verb = "verb";

            football.setNrOfPenalty(2);
            football.setNrOfScore(3);

            CloudFlatFeed feed = client.flatFeed("flat", userID);
            result[0] = feed.addCustomActivities(volley, football).join();
        });
    }

    @Test
    void removeActivityByID() {
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed = client.flatFeed("flat", userID);
            feed.removeActivityByID("654e333e-d146-11e8-bd18-1231d51167b4").join();
        });
    }

    @Test
    void removeActivityByForeignID() {
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed = client.flatFeed("flat", userID);
            feed.removeActivityByForeignID("654e333e-d146-11e8-bd18-1231d51167b4").join();
        });
    }

    @Test
    void follow() {
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed1 = client.flatFeed("flat", userID);
            CloudFlatFeed feed2 = client.flatFeed("flat", "2");
            feed1.follow(feed2).join();
        });
    }

    @Test
    void getFollowers() {
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed = client.flatFeed("flat", userID);
            feed.getFollowers().join();
        });
    }

    @Test
    void getFollowed() {
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed = client.flatFeed("flat", userID);
            feed.getFollowed().join();
        });
    }

    @Test
    void unfollow() {
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed1 = client.flatFeed("flat", userID);
            CloudFlatFeed feed2 = client.flatFeed("flat", "2");
            feed1.unfollow(feed2).join();
        });
    }
}
