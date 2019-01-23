package io.getstream.client;

import com.google.common.collect.ImmutableMap;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.*;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BatchClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void addToMany() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Activity activity = Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .build();

            client.batch().addToMany(activity, new FeedID[]{
                    new FeedID("flat", "1"),
                    new FeedID("flat", "2")
            }).join();
        });
    }

    @Test
    void followMany() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret).build().batch();

            client.followMany(0, new FollowRelation[]{
                    new FollowRelation("flat:1", "flat:2"),
                    new FollowRelation("aggregated:1", "flat:1")
            }).join();
        });
    }

    @Test
    void unfollowMany() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret).build().batch();

            client.unfollowMany(new FollowRelation[]{
                    new FollowRelation("flat:1", "flat:2"),
                    new FollowRelation("aggregated:1", "flat:1")
            }).join();
        });
    }

    @Test
    void updateActivities() {
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret).build().batch();

            client.updateActivities(Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .foreignID("foreignID")
                    .time(new Date())
                    .build()).join();
        });
    }

    @Test
    void partiallyUpdateActivityByID() {
        Activity[] result = new Activity[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Map<String, Object> set = ImmutableMap.of("value", "message");
            Iterable<String> unset = Collections.emptyList();
            result[0] = client.updateActivityByID("1657b300-a648-11d5-8080-800020fde6c3", set, unset).join();
        });
    }

    @Test
    void partiallyUpdateActivityByForeignID() {
        Activity[] result = new Activity[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date time = isoFormat.parse("2001-09-11T00:01:02.000000");

            Map<String, Object> set = ImmutableMap.of("value", "message");
            Iterable<String> unset = Collections.emptyList();
            result[0] = client.updateActivityByForeignID(new ForeignIDTimePair("foreignID", time), set, unset).join();
        });
    }

    @Test
    void partiallyUpdateActivitiesByID() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            ActivityUpdate update = ActivityUpdate.builder()
                    .id("1657b300-a648-11d5-8080-800020fde6c3")
                    .set(ImmutableMap.of("value", "message"))
                    .unset(Collections.emptyList())
                    .build();

            result[0] = client.updateActivitiesByID(update).join();
        });
    }

    @Test
    void partiallyUpdateActivitiesByForeignID() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            ActivityUpdate update = ActivityUpdate.builder()
                    .foreignID("foreignID")
                    .time(isoFormat.parse("2001-09-11T00:01:02.000000"))
                    .set(ImmutableMap.of("value", "message"))
                    .unset(Collections.emptyList())
                    .build();

            result[0] = client.updateActivitiesByForeignID(update).join();
        });
    }

    @Test
    void getActivitiesByID() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret).build().batch();

            result[0] = client.getActivitiesByID("1657b300-a648-11d5-8080-800020fde6c3").join();
        });
    }

    @Test
    void getActivitiesByForeignID() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            BatchClient client = Client.builder(apiKey, secret).build().batch();

            result[0] = client.getActivitiesByForeignID(new ForeignIDTimePair("foreignID", new Date())).join();
        });
    }
}