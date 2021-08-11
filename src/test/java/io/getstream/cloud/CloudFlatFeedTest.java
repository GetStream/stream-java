package io.getstream.cloud;

import static io.getstream.core.utils.Enrichment.createCollectionReference;
import static io.getstream.core.utils.Enrichment.createUserReference;

import io.getstream.client.Client;
import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.Data;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.Reaction;
import io.getstream.core.options.EnrichmentFlags;
import java.net.MalformedURLException;
import java.util.List;
import java8.util.concurrent.CompletionException;
import org.junit.BeforeClass;
import org.junit.Test;

public class CloudFlatFeedTest {
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
        createUserReference(
            Client.builder(apiKey, secret).build().user(userID).getOrCreate().join().getID());
  }

  @Test
  public void getActivities() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudFlatFeed feed = client.flatFeed("flat", userID);
    List<Activity> result = feed.getActivities().join();
  }

  @Test
  public void getEnrichedActivities() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    Data user = client.user(userID).get().join();
    CloudFlatFeed feed = client.flatFeed("rich", userID);
    Activity activity =
        feed.addActivity(
                Activity.builder()
                    .actor(actorID)
                    .verb("found")
                    .object(createCollectionReference("source-of-richness", "wealth"))
                    .build())
            .join();

    Reaction reaction = client.reactions().add(user.getID(), "like", activity.getID()).join();
    client.reactions().addChild(user.getID(), "like", reaction.getId()).join();

    List<EnrichedActivity> result =
        feed.getEnrichedActivities(
                new EnrichmentFlags()
                    .withOwnChildren()
                    .withUserReactions("some-user")
                    .withReactionCounts()
                    .withRecentReactions())
            .join();
  }

  @Test
  public void getCustomActivities() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudFlatFeed feed = client.flatFeed("custom", userID);

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
    List<Match> result = feed.getCustomActivities(Match.class).join();
  }

  @Test(expected = CompletionException.class)
  public void invalidFeedType() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudFlatFeed feed = client.flatFeed("aggregated", userID);
    List<Activity> result = feed.getActivities().join();
  }
}
