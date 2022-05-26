package io.getstream.cloud;

import static org.junit.Assert.fail;

import io.getstream.client.Client;
import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.faye.subscription.ChannelSubscription;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.RealtimeMessage;
import io.getstream.core.utils.Enrichment;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java8.util.concurrent.CompletableFuture;
import org.junit.BeforeClass;
import org.junit.Test;

public class CloudFeedTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");
  private static final String appId =
      System.getenv("STREAM_APP_ID") != null
          ? System.getenv("STREAM_APP_ID")
          : System.getProperty("STREAM_APP_ID");
  private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e1";
  private static final Token token = buildToken();
  private static String actorID;

  private static Token buildToken() {
    try {
      return Client.builder(apiKey, secret).build().frontendToken(userID);
    } catch (MalformedURLException e) {
      e.printStackTrace();
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
    CloudFlatFeed feed = client.flatFeed("flat", userID);
    Activity result = feed.addActivity(activity).join();
  }

  @Test
  public void addActivities() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    Activity activity = Activity.builder().actor(actorID).verb("test").object("test").build();
    CloudFlatFeed feed = client.flatFeed("flat", userID);
    List<Activity> result = feed.addActivities(activity).join();
  }

  @Test
  public void addCustomActivities() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    VolleyballMatch volley = new VolleyballMatch();
    volley.actor = actorID;
    volley.object = "Message";
    volley.verb = "verb";
    volley.setNrOfBlocked(1);
    volley.setNrOfServed(1);

    FootballMatch football = new FootballMatch();
    football.actor = actorID;
    football.object = "Message";
    football.verb = "verb";

    football.setNrOfPenalty(2);
    football.setNrOfScore(3);

    CloudFlatFeed feed = client.flatFeed("flat", userID);
    List<Match> result = feed.addCustomActivities(volley, football).join();
  }

  @Test
  public void removeActivityByID() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    Activity activity = Activity.builder().actor(actorID).verb("test").object("test").build();
    CloudFlatFeed feed = client.flatFeed("flat", userID);
    Activity result = feed.addActivity(activity).join();

    feed.removeActivityByID(result.getID()).join();
  }

  @Test
  public void removeActivityByForeignID() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    Date now = new Date();
    String foreignID = "some-foreign-id";
    Activity activity =
        Activity.builder()
            .actor(actorID)
            .verb("test")
            .object("test")
            .foreignID(foreignID)
            .time(now)
            .build();
    CloudFlatFeed feed = client.flatFeed("flat", userID);
    Activity result = feed.addActivity(activity).join();

    feed.removeActivityByForeignID(foreignID).join();
  }

  @Test
  public void follow() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudFlatFeed feed1 = client.flatFeed("flat", userID);
    CloudFlatFeed feed2 = client.flatFeed("flat", "2");
    feed1.follow(feed2).join();
  }

  @Test
  public void getFollowers() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudFlatFeed feed = client.flatFeed("flat", userID);
    feed.getFollowers().join();
  }

  @Test
  public void getFollowed() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudFlatFeed feed = client.flatFeed("flat", userID);
    feed.getFollowed().join();
  }

  @Test
  public void unfollow() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudFlatFeed feed1 = client.flatFeed("flat", userID);
    CloudFlatFeed feed2 = client.flatFeed("flat", "2");
    feed1.unfollow(feed2).join();
  }

  @Test
  public void TestFaye() throws Exception {
    AtomicReference<RealtimeMessage> msg = new AtomicReference<>();
    CloudClient client = CloudClient.builder(apiKey, token, userID, appId).build();
    CloudFlatFeed feed = client.flatFeed("user", userID);
    CompletableFuture<ChannelSubscription> subscription =
        feed.subscribe(message -> msg.set(message));

    feed.addActivity(
            Activity.builder()
                .actor("SU:" + userID)
                .verb("tweet")
                .object("tweet:10")
                .extraField("message", "Beautiful bird!")
                .build())
        .get();

    for (int i = 0; i < 25; i++) {
      if (msg.get() != null) {
        subscription.cancel(true);
        return;
      }
      Thread.sleep(250);
    }

    subscription.cancel(true);
    fail("Timeout. Did not receive message through websocket in time.");
  }
}
