package io.getstream.client;

import com.google.common.collect.Lists;
import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void addActivity() {
        Activity[] result = new Activity[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            Activity activity = Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .build();
            FlatFeed feed = client.flatFeed("flat", "1");
            result[0] = feed.addActivity(activity).join();
        });
    }

    @Test
    void addActivities() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            Activity activity = Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .build();
            FlatFeed feed = client.flatFeed("flat", "1");
            result[0] = feed.addActivities(activity).join();
        });
    }

    @Test
    void addCustomActivities() {
        List<Match>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            VolleyballMatch volley = new VolleyballMatch();
            volley.actor = "Me";
            volley.object = "Message";
            volley.verb = "verb";
            volley.setNrOfBlocked(1);
            volley.setNrOfServed(1);

            FootballMatch football = new FootballMatch();
            football.actor = "Me";
            football.object = "Message";
            football.verb = "verb";

            football.setNrOfPenalty(2);
            football.setNrOfScore(3);

            FlatFeed feed = client.flatFeed("flat", "1");
            result[0] = feed.addCustomActivities(volley, football).join();
        });
    }

    @Test
    void removeActivityByID() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            feed.removeActivityByID("654e333e-d146-11e8-bd18-1231d51167b4").join();
        });
    }

    @Test
    void removeActivityByForeignID() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            feed.removeActivityByForeignID("654e333e-d146-11e8-bd18-1231d51167b4").join();
        });
    }

    @Test
    void follow() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed1 = client.flatFeed("flat", "1");
            FlatFeed feed2 = client.flatFeed("flat", "2");
            feed1.follow(feed2).join();
        });
    }

    @Test
    void getFollowers() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            feed.getFollowers().join();
        });
    }

    @Test
    void getFollowed() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            feed.getFollowed().join();
        });
    }

    @Test
    void unfollow() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed1 = client.flatFeed("flat", "1");
            FlatFeed feed2 = client.flatFeed("flat", "2");
            feed1.unfollow(feed2).join();
        });
    }

    @Test
    void updateActivityToTargets() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            Activity activity = Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .foreignID("foreignID")
                    .time(new Date())
                    .to(Lists.newArrayList(new FeedID("feed:2")))
                    .build();
            feed.updateActivityToTargets(activity, new FeedID[] { new FeedID("feed:3") }, new FeedID[] { new FeedID("feed:2") });
        });
    }

    @Test
    void replaceActivityToTargets() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            Activity activity = Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .foreignID("foreignID")
                    .time(new Date())
                    .to(Lists.newArrayList(new FeedID("feed:2")))
                    .build();
            feed.replaceActivityToTargets(activity, new FeedID("feed:3"));
        });
    }
}
