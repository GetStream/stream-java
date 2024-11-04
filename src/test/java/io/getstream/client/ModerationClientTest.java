package io.getstream.client;

import static org.junit.Assert.*;

import io.getstream.core.http.Response;
import io.getstream.core.models.*;
import io.getstream.core.models.Activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
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
  public void setUp() throws Exception {
    client = Client.builder(apiKey, secret).build();
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

    Response flagResponse =
        moderationClient.flagActivity(activityResponse.getID(), "vishal", "blood", null).join();
    assertNotNull(flagResponse);
  }

  @Test
  public void testFlagReaction() throws Exception {
    ModerationClient moderationClient = client.moderation();

    Activity activity = Activity.builder().actor("test").verb("test").object("test").build();

    Activity activityResponse = client.flatFeed("flat", "1").addActivity(activity).join();
    assertNotNull(activityResponse);

    Reaction reactionResponse =
        client.reactions().add("test", "like", activityResponse.getID()).join();
    assertNotNull(reactionResponse);

    Response flagResponse =
        moderationClient.flagReaction(reactionResponse.getId(), "test", "blood", null).join();
    assertNotNull(flagResponse);
    assertEquals(201, flagResponse.getCode());
    assertNotNull(flagResponse);

  }

  @Test
  public void testActivityModerated() throws Exception {

    ModerationClient moderationClient = client.moderation();

    String[] images = new String[] {"image1", "image2"};
    Activity activity =
        Activity.builder()
            .actor("test")
            .verb("test")
            .object("test")
            .moderationTemplate("moderation_template_activity")
            .extraField("text", "pissoar")
            .extraField("attachment", images)
            .foreignID("for")
            .time(new Date())
            .build();

    Activity activityResponse = client.flatFeed("user", "1").addActivity(activity).join();
    assertNotNull(activityResponse);
    ModerationResponse m = activityResponse.getModerationResponse();
    assertEquals(m.getStatus(), "complete");
    assertEquals(m.getRecommendedAction(), "remove");
  }

  @Test
  public void testActivityModeratedReactions() throws Exception {

    ModerationClient moderationClient = client.moderation();

    String[] images = new String[] {"image1", "image2"};
    Activity activity =
        Activity.builder()
            .actor("test")
            .verb("test")
            .object("test")
            .extraField("text", "pissoar")
            .extraField("attachment", images)
            .foreignID("for")
            .time(new Date())
            .build();

    Activity activityResponse = client.flatFeed("user", "1").addActivity(activity).join();
    assertNotNull(activityResponse);

    Reaction r =
        Reaction.builder()
            .kind("like")
            .activityID(activityResponse.getID())
            .userID("user123")
            .extraField("text", "pissoar")
            .moderationTemplate("moderation_template_reaction")
            .build();

    Reaction reactionResponse = client.reactions().add("user", r).join();
    ModerationResponse m = reactionResponse.getModerationResponse();
    assertEquals(m.getStatus(), "complete");
    assertEquals(m.getRecommendedAction(), "remove");
  }
}
