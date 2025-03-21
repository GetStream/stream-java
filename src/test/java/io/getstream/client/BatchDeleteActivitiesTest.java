package io.getstream.client;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import io.getstream.core.http.Response;
import io.getstream.core.models.*;
import io.getstream.core.models.BatchDeleteActivitiesRequest.ActivityToDelete;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Limit;
import java8.util.concurrent.CompletableFuture;
import org.junit.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BatchDeleteActivitiesTest {

    private static final String apiKey =
            System.getenv("STREAM_KEY") != null
                    ? System.getenv("STREAM_KEY")
                    : System.getProperty("STREAM_KEY");
    private static final String secret =
            System.getenv("STREAM_SECRET") != null
                    ? System.getenv("STREAM_SECRET")
                    : System.getProperty("STREAM_SECRET");

    private Client client;

    @Before
    public void setUp() throws Exception {
        client = Client.builder(apiKey, secret).build();
    }

    @Test
    public void testDeleteActivities() throws Exception {
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        FlatFeed feed = client.flatFeed("flat", uuid1);

        // Insert some activities
        Activity activity1 = Activity.builder()
                .actor("user1")
                .verb("post")
                .object("object1")
                .build();
        Activity activity1Res = feed.addActivity(activity1).join();

        Activity activity2 = Activity.builder()
                .actor("user1")
                .verb("like")
                .object("object2")
                .build();
        Activity activity2Res = feed.addActivity(activity2).join();

        // Create delete request
        List<ActivityToDelete> activities = Arrays.asList(
                new ActivityToDelete(activity1Res.getID(), Arrays.asList("user:user1", "user:alice")),
                new ActivityToDelete(activity2Res.getID(), Arrays.asList("user:user1"))
        );
        BatchClient clientBatch = Client.builder(apiKey, secret).build().batch();

        // Verify activities are inserted
        List<Activity> activity1Resp = clientBatch.getActivitiesByID(activity1Res.getID()).join();
        assertEquals(1, activity1Resp.size());

        List<Activity> activity2Resp = clientBatch.getActivitiesByID(activity2Res.getID()).join();
        assertEquals(1, activity2Resp.size());

        BatchDeleteActivitiesRequest request = new BatchDeleteActivitiesRequest(activities);

        // Delete activities
        CompletableFuture<Object> future = client.deleteActivities(request);
        future.join();

        assertTrue(future.isDone());

        // Verify activities are deleted
        List<Activity> deletedActivity1 = clientBatch.getActivitiesByID(activity1Res.getID()).join();
        assertEquals(0, deletedActivity1.size());

        List<Activity> deletedActivity2 = clientBatch.getActivitiesByID(activity2Res.getID()).join();
        assertEquals(0, deletedActivity2.size());
    }

    @Test
    public void testDeleteReactions() throws Exception {
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        FlatFeed feed = client.flatFeed("user", uuid1);

        // Insert some activities
        Activity activity1 = Activity.builder()
                .actor("user1")
                .verb("post")
                .object("object1")
                .build();
        Activity activity1Res = feed.addActivity(activity1).join();

        Activity activity2 = Activity.builder()
                .actor("user1")
                .verb("like")
                .object("object2")
                .build();
        Activity activity2Res = feed.addActivity(activity2).join();

        //add reactions for activity1
        Reaction u1=client.reactions().add("user1", "like", activity1Res.getID()).join();
        Reaction u2=client.reactions().add("user2", "like", activity1Res.getID()).join();

        Reaction u3=client.reactions().add("user1", "like", activity2Res.getID()).join();
        Reaction u4=client.reactions().add("user2", "like", activity2Res.getID()).join();


        //fetch test reactions
        Reaction r1=client.reactions().get(u1.getId()).join();
        assertNotNull(r1);

        Reaction r2=client.reactions().get(u2.getId()).join();
        assertNotNull(r2);

        Reaction r3=client.reactions().get(u3.getId()).join();
        assertNotNull(r3);

        Reaction r4=client.reactions().get(u4.getId()).join();
        assertNotNull(r4);

        // Create reaction delete request
        BatchDeleteReactionsRequest deleteReactionsRequest=
                new BatchDeleteReactionsRequest(Arrays.asList
                        (u1.getId(), u2.getId(), u3.getId()));


        client.deleteReactions(deleteReactionsRequest).join();

        //fetch test reactions

        // DoesNotExistException
        assertThrows(Exception.class, () -> {
            client.reactions().get(u1.getId()).join();
        });
        assertThrows(Exception.class, () -> {
            client.reactions().get(u2.getId()).join();
        });
        assertThrows(Exception.class, () -> {
            client.reactions().get(u3.getId()).join();
        });


    }
    @Test
    public void testSoftDeleteReactions() throws Exception {
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        FlatFeed feed = client.flatFeed("user", uuid1);

        // Insert two activities
        Activity activity1Res = feed.addActivity(Activity.builder()
                .actor("user1")
                .verb("post")
                .object("object1")
                .build()).join();

        Activity activity2Res = feed.addActivity(Activity.builder()
                .actor("user1")
                .verb("like")
                .object("object2")
                .build()).join();

        // Add reactions to both activities
        Reaction u1 = client.reactions().add("user1", "like", activity1Res.getID()).join();
        Reaction u2 = client.reactions().add("user2", "like", activity1Res.getID()).join();
        Reaction u3 = client.reactions().add("user1", "like", activity2Res.getID()).join();
        Reaction u4 = client.reactions().add("user2", "like", activity2Res.getID()).join();

        // Verify reactions were created
        assertNotNull(client.reactions().get(u1.getId()).join());
        assertNotNull(client.reactions().get(u2.getId()).join());
        assertNotNull(client.reactions().get(u3.getId()).join());
        assertNotNull(client.reactions().get(u4.getId()).join());

        // Soft delete reactions
        BatchDeleteReactionsRequest deleteReactionsRequest = 
                new BatchDeleteReactionsRequest(Arrays.asList(u1.getId(), u2.getId(), u3.getId()), true);
        client.deleteReactions(deleteReactionsRequest).join();

        // Verify reactions can still be fetched
        assertNotNull(client.reactions().get(u1.getId()).join());
        assertNotNull(client.reactions().get(u2.getId()).join());
        assertNotNull(client.reactions().get(u3.getId()).join());
    }

    @Test
    public void testDeleteActivitiesMultipleFeeds() throws Exception {

        // Insert some activities
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        String uuid2 = UUID.randomUUID().toString().replace("-", "");

        FlatFeed feed = client.flatFeed("flat", uuid1);
        FlatFeed feedAlice = client.flatFeed("flat", uuid2);
        Activity activity1 = Activity.builder()
                .actor("user1")
                .verb("post")
                .to(Lists.newArrayList(feedAlice.getID()))
                .object("object1")
                .build();
        Activity activity1Res = feed.addActivity(activity1).join();

        Activity activity2 = Activity.builder()
                .actor("user1")
                .verb("like")
                .to(Lists.newArrayList(feedAlice.getID()))
                .object("object2")
                .build();
        Activity activity2Res = feed.addActivity(activity2).join();

        // Verify activities are inserted
        List<Activity> activities =feed.getActivities(
                new Limit(69), new Filter()).join();
        assertEquals(2, activities.size());

        // Create delete request
        List<ActivityToDelete> activitiesToDelete = Arrays.asList(
                new ActivityToDelete(activity1Res.getID(), Arrays.asList(feed.getID().toString())),
                new ActivityToDelete(activity2Res.getID(), Arrays.asList(feedAlice.getID().toString()))
        );

        activities =feedAlice.getActivities(
                new Limit(10), new Filter()).join();
        assertEquals(2, activities.size());//0

        activities =feedAlice.getActivities(
                new Limit(10), new Filter().discardDeletedActivities()).join();
        assertEquals(2, activities.size());//1


        BatchDeleteActivitiesRequest request = new BatchDeleteActivitiesRequest(activitiesToDelete);
        CompletableFuture<Object> future = client.deleteActivities(request);
        future.join();


        // Verify activities are deleted by fetching by ID
        BatchClient clientBatch = Client.builder(apiKey, secret).build().batch();
        List<Activity> deletedActivity1 = clientBatch.getActivitiesByID(activity1Res.getID()).join();
        assertEquals(0, deletedActivity1.size());

        // read feeds
        // without discardDeletedActivities
        activities =feed.getActivities(
                new Limit(69), new Filter()).join();
        assertEquals(2, activities.size());


        activities =feed.getActivities(
                new Limit(10), new Filter().discardDeletedActivities()).join();
        assertEquals(0, activities.size());

        activities =feedAlice.getActivities(
                new Limit(10), new Filter()).join();
        assertEquals(2, activities.size());

        activities =feedAlice.getActivities(
                new Limit(10), new Filter().discardDeletedActivities()).join();
        assertEquals(0, activities.size());
    }
}