package io.getstream.client;

import com.google.common.collect.ImmutableMap;
import io.getstream.core.KeepHistory;
import io.getstream.core.models.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

public class BatchClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    public void addToMany() throws Exception {
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
    }

    @Test
    public void followMany() throws Exception {
        BatchClient client = Client.builder(apiKey, secret).build().batch();

        client.followMany(0, new FollowRelation[]{
                new FollowRelation("flat:1", "flat:2"),
                new FollowRelation("aggregated:1", "flat:1")
        }).join();
    }

    @Test
    public void unfollowMany() throws Exception {
        BatchClient client = Client.builder(apiKey, secret).build().batch();

        client.unfollowMany(new FollowRelation[]{
                new FollowRelation("flat:1", "flat:2"),
                new FollowRelation("aggregated:1", "flat:1")
        }).join();

        client.unfollowMany(KeepHistory.NO, new FollowRelation[]{
                new FollowRelation("flat:1", "flat:2"),
                new FollowRelation("aggregated:1", "flat:1")
        }).join();

        client.unfollowMany(new UnfollowOperation[]{
                new UnfollowOperation("flat:1", "flat:2", KeepHistory.NO),
                new UnfollowOperation("aggregated:1", "flat:1", KeepHistory.YES)
        }).join();
    }

    @Test
    public void updateActivities() throws Exception {
        Client client = Client.builder(apiKey, secret).build();

        Activity activity = Activity.builder()
                .actor("test")
                .verb("test")
                .object("test")
                .foreignID("foreignID")
                .time(new Date())
                .build();
        FlatFeed feed = client.flatFeed("flat", "1");
        Activity result = feed.addActivity(activity).join();

        client.batch().updateActivities(Activity.builder()
                .fromActivity(result)
                .build()).join();
    }

    @Test
    public void partiallyUpdateActivityByID() throws Exception {
        Client client = Client.builder(apiKey, secret).build();

        Map<String, Object> set = ImmutableMap.of("value", "message");
        Iterable<String> unset = Collections.emptyList();
        Activity result = client.updateActivityByID("1657b300-a648-11d5-8080-800020fde6c3", set, unset).join();
    }

    @Test
    public void partiallyUpdateActivityByForeignID() throws Exception {
        Client client = Client.builder(apiKey, secret).build();

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date time = isoFormat.parse("2001-09-11T00:01:02.000000");

        Map<String, Object> set = ImmutableMap.of("value", "message");
        Iterable<String> unset = Collections.emptyList();
        Activity result = client.updateActivityByForeignID(new ForeignIDTimePair("foreignID", time), set, unset).join();
    }

    @Test
    public void partiallyUpdateActivitiesByID() throws Exception {
        Client client = Client.builder(apiKey, secret).build();

        ActivityUpdate update = ActivityUpdate.builder()
                .id("1657b300-a648-11d5-8080-800020fde6c3")
                .set(ImmutableMap.of("value", "message"))
                .unset(Collections.emptyList())
                .build();

        List<Activity> result = client.updateActivitiesByID(update).join();
    }

    @Test
    public void partiallyUpdateActivitiesByForeignID() throws Exception {
        Client client = Client.builder(apiKey, secret).build();

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        ActivityUpdate update = ActivityUpdate.builder()
                .foreignID("foreignID")
                .time(isoFormat.parse("2001-09-11T00:01:02.000000"))
                .set(ImmutableMap.of("value", "message"))
                .unset(Collections.emptyList())
                .build();

        List<Activity> result = client.updateActivitiesByForeignID(update).join();
    }

    @Test
    public void getActivitiesByID() throws Exception {
        BatchClient client = Client.builder(apiKey, secret).build().batch();

        List<Activity> result = client.getActivitiesByID("1657b300-a648-11d5-8080-800020fde6c3").join();
    }

    @Test
    public void getActivitiesByForeignID() throws Exception {
        BatchClient client = Client.builder(apiKey, secret).build().batch();

        List<Activity> result = client.getActivitiesByForeignID(new ForeignIDTimePair("foreignID", new Date())).join();
    }
}