package io.getstream.client;

import io.getstream.client.Client;
import io.getstream.client.ModerationClient;
import io.getstream.core.models.Activity;
import io.getstream.core.models.Data;
import io.getstream.core.models.Reaction;
import io.getstream.core.http.Response;
import static org.junit.Assert.*;


import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.junit.*;


public class ModerationClientTest {

    private static final String apiKey =
            System.getenv("STREAM_KEY") != null
                    ? System.getenv("STREAM_KEY")
                    : System.getProperty("STREAM_KEY");
    private static final String secret =
            System.getenv("STREAM_SECRET") != null
                    ? System.getenv("STREAM_SECRET")
                    : System.getProperty("STREAM_SECRET");


    @Test
    public void testFlagUser() throws Exception {
        Client client = Client.builder(apiKey, secret).build();
        ModerationClient moderationClient = client.moderation();

        String userId = UUID.randomUUID().toString();
        User user = client.user(userId);
        user.getOrCreate().join();
        Data result = user.get().join();

        Response flagResponse = moderationClient.flagUser(userId, "blood", null).join();
        assertNotNull(flagResponse);
    }

    @Test
    public void testFlagActivity() throws Exception {
        Client client = Client.builder(apiKey, secret).build();
        ModerationClient moderationClient = client.moderation();

        Activity activity = Activity.builder().actor("test").verb("test").object("test").build();

        Activity activityResponse = client.flatFeed("flat", "1").addActivity(activity).join();
        assertNotNull(activityResponse);

        Response flagResponse = moderationClient.flagActivity(activityResponse.getID(), "vishal", "blood", null).join();
        assertNotNull(flagResponse);
    }

    @Test
    public void testFlagReaction() throws Exception {
        Client client = Client.builder(apiKey, secret).build();
        ModerationClient moderationClient = client.moderation();

        Activity activity = Activity.builder().actor("test").verb("test").object("test").build();

        Activity activityResponse = client.flatFeed("flat", "1").addActivity(activity).join();
        assertNotNull(activityResponse);

        Reaction reactionResponse = client.reactions().add("user123","like", activityResponse.getID()).join();
        assertNotNull(reactionResponse);

        Response flagResponse = moderationClient.flagReaction(reactionResponse.getId(), "bobby", "blood", null).join();
        assertNotNull(flagResponse);
    }
}