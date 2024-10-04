package io.getstream.client;

import io.getstream.core.models.*;
import io.getstream.core.models.Activity;
import io.getstream.core.http.Response;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.UUID;
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

    Client client;
    @Before
    public void setUp()throws Exception {

        System.out.println("api key-------------------------------------------------");
        System.out.println(addSpacesAfterEachChar(apiKey));
//        client =
//                Client.builder(apiKey, secret)
//                        .scheme("http")
//                        .host("localhost")
//                        .port(18000)
//                        .build();
        client= Client.builder(apiKey, secret).build();
    }
    public static String addSpacesAfterEachChar(String str) {
        StringBuilder stringBuilder = new StringBuilder(); // Using StringBuilder for efficiency

        for (int i = 0; i < str.length(); i++) {
            stringBuilder.append(str.charAt(i)); // Append the character
            stringBuilder.append(' '); // Append a space
        }

        return stringBuilder.toString().trim(); // Convert to string and remove the trailing space
    }
    @Test
    public void testFlagUser() throws Exception {

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
        ModerationClient moderationClient = client.moderation();

        Activity activity = Activity.builder().actor("test").verb("test").object("test").build();

        Activity activityResponse = client.flatFeed("flat", "1").addActivity(activity).join();
        assertNotNull(activityResponse);

        Response flagResponse = moderationClient.flagActivity(activityResponse.getID(), "vishal", "blood", null).join();
        assertNotNull(flagResponse);
    }

    @Test
    public void testFlagReaction() throws Exception {
        ModerationClient moderationClient = client.moderation();

        Activity activity = Activity.builder().actor("test").verb("test").object("test").build();

        Activity activityResponse = client.flatFeed("flat", "1").addActivity(activity).join();
        assertNotNull(activityResponse);

        Reaction reactionResponse = client.reactions().add("user123","like", activityResponse.getID()).join();
        assertNotNull(reactionResponse);

        Response flagResponse = moderationClient.flagReaction(reactionResponse.getId(), "bobby", "blood", null).join();
        assertNotNull(flagResponse);
    }
    @Test
    public void testActivityModerated() throws Exception {

        ModerationClient moderationClient = client.moderation();

        String[] images = new String[] { "image1", "image2" };
        Activity activity = Activity.builder().
                actor("test").
                verb("test").
                object("test").
                moderationTemplate("moderation_template_test_7").
                extraField("text", "pissoar").
                extraField("attachment", images).
                foreignID("for").
                time(new Date()).
                build();

        Activity activityResponse = client.flatFeed("user", "1").addActivity(activity).join();
        assertNotNull(activityResponse);
        ModerationResponse m=activityResponse.getModerationResponse();
        assertEquals(m.getStatus(), "complete");
        assertEquals(m.getRecommendedAction(), "remove");
    }
    @Test
        public void testActivityModeratedReactions() throws Exception {

            ModerationClient moderationClient = client.moderation();

            String[] images = new String[] { "image1", "image2" };
            Activity activity = Activity.builder().
                    actor("test").
                    verb("test").
                    object("test").
                    extraField("text", "pissoar").
                    extraField("attachment", images).
                    foreignID("for").
                    time(new Date()).
                    build();

            Activity activityResponse = client.flatFeed("user", "1").addActivity(activity).join();
            assertNotNull(activityResponse);

            Reaction r=Reaction.builder().
                        kind("like").
                        activityID(activityResponse.getID()).
                        userID("user123").
                        extraField("p","pissoar").
                        moderationTemplate("reaction_test_7").
                        build();

            Reaction reactionResponse = client.reactions().add("user", r).join();
            ModerationResponse m=reactionResponse.getModerationResponseFromMap();
            assertEquals(m.getStatus(), "complete");
            assertEquals(m.getRecommendedAction(), "remove");
        }
}
