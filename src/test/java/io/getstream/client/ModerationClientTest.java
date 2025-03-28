package io.getstream.client;

import static org.junit.Assert.*;

import io.getstream.core.http.Response;
import io.getstream.core.models.*;
import io.getstream.core.models.Activity;

import java.sql.Time;
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
  public void testFlagActivity() throws Exception {
    ModerationClient moderationClient = client.moderation();

    Activity activity = Activity.builder().actor("bad-user").verb("test").object("test").moderationTemplate("moderation_template_activity").time(new Date()).foreignID("fid").build();
    Activity activityResponse = client.flatFeed("user", "1").addActivity(activity).join();

    String reportingUser="reporting-user";
    Activity activity1 = Activity.builder().actor(reportingUser).verb("verb").object("test").moderationTemplate("moderation_template_activity").time(new Date()).foreignID("fid").build();
    Activity activityResponse1 = client.flatFeed("user", "1").addActivity(activity1).join();
    assertNotNull(activityResponse);

    Response flagResponse =
        moderationClient.flagActivity(activityResponse.getID(), reportingUser, "blood", null).join();
    assertNotNull(flagResponse);
    assertEquals(201, flagResponse.getCode());
  }

  @Test
  public void testFlagUser() throws Exception {

    ModerationClient moderationClient = client.moderation();

    Response flagResponse = moderationClient.flagUser("bad-user", "reporting-user", "blood", null).join();
    assertNotNull(flagResponse);
    assertEquals(201, flagResponse.getCode());
  }

  @Test
  public void testFlagReaction() throws Exception {
    ModerationClient moderationClient = client.moderation();

    Activity activity = Activity.builder().actor("bad-user").verb("test").object("test").moderationTemplate("moderation_template_reaction").time(new Date()).foreignID("fid").build();

    Activity activityResponse = client.flatFeed("flat", "1").addActivity(activity).join();
    assertNotNull(activityResponse);

    Reaction r = Reaction.builder().activityID(activityResponse.getID()).kind("like").userID("bad-user").moderationTemplate("moderation_template_reaction").build();
    Reaction reactionResponse = client.reactions().add("bad-user", r).join();
    assertNotNull(reactionResponse);

    Response flagResponse =
        moderationClient.flagReaction(reactionResponse.getId(), "reporting-user", "blood", null).join();
    assertNotNull(flagResponse);
    assertEquals(201, flagResponse.getCode());

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
