package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.Group;
import io.getstream.core.models.Reaction;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.utils.Enrichment;
import java.net.MalformedURLException;
import java.util.List;
import java8.util.concurrent.CompletionException;
import org.junit.BeforeClass;
import org.junit.Test;

public class CloudAggregatedFeedTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");
  private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e2";
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

    CloudAggregatedFeed feed = client.aggregatedFeed("aggregated", userID);
    Activity result = feed.addActivity(activity).join();
  }

  @Test
  public void getActivityGroups() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudAggregatedFeed feed = client.aggregatedFeed("aggregated", userID);
    List<? extends Group<Activity>> result = feed.getActivities().join();
  }

  @Test
  public void getEnrichedActivityGroups() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudAggregatedFeed feed = client.aggregatedFeed("rich_aggregated", userID);
    Activity activity =
        feed.addActivity(
                Activity.builder()
                    .actor(actorID)
                    .verb("post")
                    .object("text")
                    .extraField("text", "Hello world!")
                    .build())
            .join();
    client
        .reactions()
        .add(
            userID,
            Reaction.builder()
                .activityID(activity.getID())
                .userID(userID)
                .kind("comment")
                .extraField("text", "Hi there!")
                .build(),
            feed.getID())
        .join();
    List<? extends Group<EnrichedActivity>> result =
        feed.getEnrichedActivities(
                new EnrichmentFlags().withReactionCounts().withOwnReactions().withRecentReactions())
            .join();
  }

  @Test
  public void getCustomActivityGroups() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudAggregatedFeed feed = client.aggregatedFeed("custom_aggregated", userID);

    VolleyballMatch volley = new VolleyballMatch();
    volley.actor = actorID;
    volley.object = "Message";
    volley.verb = "verb";
    volley.setNrOfBlocked(1);
    volley.setNrOfServed(1);
    feed.addCustomActivity(volley).join();

    FootballMatch football = new FootballMatch();
    football.actor = actorID;
    football.object = "Message";
    football.verb = "verb";

    football.setNrOfPenalty(2);
    football.setNrOfScore(3);
    feed.addCustomActivity(football).join();
    List<? extends Group<Match>> result = feed.getCustomActivities(Match.class).join();
  }

  @Test(expected = CompletionException.class)
  public void invalidFeedType() throws Exception {
    List<? extends Group<Activity>>[] result = new List[1];
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudAggregatedFeed feed = client.aggregatedFeed("flat", userID);
    result[0] = feed.getActivities().join();
  }
}
