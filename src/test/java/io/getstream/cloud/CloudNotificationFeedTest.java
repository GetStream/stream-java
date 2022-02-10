package io.getstream.cloud;

import static org.junit.Assert.*;

import io.getstream.client.Client;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.PaginatedNotificationGroup;
import io.getstream.core.utils.Enrichment;
import java.net.MalformedURLException;
import org.junit.BeforeClass;
import org.junit.Test;

public class CloudNotificationFeedTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");
  private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e1";
  private static final Token token = buildToken();
  private static String actorID;

  private static Token buildToken() {
    try {
      return Client.builder(apiKey, secret).build().frontendToken(userID);
    } catch (MalformedURLException e) {
      return null;
    }
  }

  @BeforeClass
  public static void setup() throws Exception {
    actorID =
        Enrichment.createUserReference(
            Client.builder(apiKey, secret).build().user(userID).getOrCreate().join().getID());
  }

  @Test
  public void addActivity() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    Activity activity = Activity.builder().actor(actorID).verb("test").object("test").build();
    CloudNotificationFeed feed = client.notificationFeed("notification", userID);
    Activity result = feed.addActivity(activity).join();
  }

  @Test
  public void getActivityGroups() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudNotificationFeed feed = client.notificationFeed("notification", userID);
    PaginatedNotificationGroup<Activity> result = feed.getActivities().join();
    assertFalse(result.getResults().isEmpty());
    assertNotNull(result.getResults().get(0).getID());
  }

  @Test
  public void getEnrichedActivityGroups() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudNotificationFeed feed = client.notificationFeed("rich_notification", userID);
    PaginatedNotificationGroup<EnrichedActivity> result = feed.getEnrichedActivities().join();
  }
}
