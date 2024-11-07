package io.getstream.client;

import static org.junit.Assert.*;

import io.getstream.core.BatchDeleteActivities;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.models.BatchDeleteActivitiesRequest;
import io.getstream.core.models.BatchDeleteActivitiesRequest.ActivityToDelete;
import io.getstream.core.models.Activity;
import java8.util.concurrent.CompletableFuture;
import org.junit.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

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
        BatchDeleteActivitiesClient batchDeleteClient = client.batchDeleteActivities();

        // Insert some activities
        Activity activity1 = Activity.builder()
                .actor("user1")
                .verb("post")
                .object("object1")
                .build();
        Activity activity1Res = client.flatFeed("user", "user1").addActivity(activity1).join();

        Activity activity2 = Activity.builder()
                .actor("user1")
                .verb("like")
                .object("object2")
                .build();
        Activity activity2Res = client.flatFeed("user", "user1").addActivity(activity2).join();

        // Create delete request
        List<ActivityToDelete> activities = Arrays.asList(
                new ActivityToDelete(activity1Res.getID(), Arrays.asList("user1")),
                new ActivityToDelete(activity2Res.getID(), Arrays.asList("user1"))
        );
        BatchClient clientBatch = Client.builder(apiKey, secret).build().batch();

        // Verify activities are inserted
        List<Activity> activity1Resp = clientBatch.getActivitiesByID(activity1Res.getID()).join();
        assertEquals(1, activity1Resp.size());

        List<Activity> activity2Resp = clientBatch.getActivitiesByID(activity2Res.getID()).join();
        assertEquals(1, activity2Resp.size());

        BatchDeleteActivitiesRequest request = new BatchDeleteActivitiesRequest(activities);

        // Delete activities
        CompletableFuture<Object> future = batchDeleteClient.deleteActivities(request);
        future.join();

        assertTrue(future.isDone());

        // Verify activities are deleted
        List<Activity> deletedActivity1 = clientBatch.getActivitiesByID(activity1Res.getID()).join();
        assertEquals(0, deletedActivity1.size());

        List<Activity> deletedActivity2 = clientBatch.getActivitiesByID(activity2Res.getID()).join();
        assertEquals(0, deletedActivity2.size());
    }
}