package io.getstream.client;

import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.FollowRelation;
import io.getstream.core.models.ForeignIDTimePair;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BatchClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void addToMany() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            Activity activity = Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .build();
            client.batch().addToMany(activity, new FeedID[] {
                    new FeedID("flat", "1"),
                    new FeedID("flat", "2")
            });
        });
    }

    @Test
    void followMany() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build()
                    .batch();

            client.followMany(0, new FollowRelation("flat:1", "flat:2"), new FollowRelation("aggregated:1", "flat:1"));
        });
    }

    @Test
    void unfollowMany() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build()
                    .batch();

            client.unfollowMany(new FollowRelation("flat:1", "flat:2"), new FollowRelation("aggregated:1", "flat:1"));
        });
    }

    @Test
    void updateActivities() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build()
                    .batch();

            client.updateActivities(Activity.builder()
                    .id("54a60c1e-4ee3-494b-a1e3-50c06acb5ed4")
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .build());
        });
    }

    @Test
    void getActivitiesByID() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build()
                    .batch();

            client.getActivitiesByID("54a60c1e-4ee3-494b-a1e3-50c06acb5ed4");
        });
    }

    @Test
    void getActivitiesByForeignID() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build()
                    .batch();

            client.getActivitiesByForeignID(new ForeignIDTimePair("foreignID", new Date()));
        });
    }
}