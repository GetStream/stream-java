package io.getstream.client;

import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.Data;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.options.EnrichmentFlags;
import java.util.Collections;
import java.util.List;
import java8.util.concurrent.CompletionException;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class FlatFeedTest {
  private static final String apiKey = System.getenv("STREAM_KEY") != null ? System.getenv("STREAM_KEY")
      : System.getProperty("STREAM_KEY");
  private static final String secret = System.getenv("STREAM_SECRET") != null ? System.getenv("STREAM_SECRET")
      : System.getProperty("STREAM_SECRET");

  @Test
  public void getActivities() throws Exception {
    Client client = Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();

    int takeId = 1;
    FlatFeed feed = client.flatFeed("flat", "1");
    List<Activity> result = feed.getActivities().join();
  }

  @Test
  public void getEnrichedActivities() throws Exception {
    Client client = Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();

    Data user = client.user("john-doe").getOrCreate(new Data().set("hey", "now")).join();
    FlatFeed feed = client.flatFeed("flat", "rich");

    List<EnrichedActivity> result = feed.getEnrichedActivities(new EnrichmentFlags().withOwnChildren()
        .withUserReactions("some-user").withReactionCounts().withRecentReactions()).join();
    Collections.sort(result, (a, b) -> {
      Number aValue = a.getReactionCounts().get("like");
      Number bValue = b.getReactionCounts().get("like");
      int aLikes = aValue == null ? 0 : aValue.intValue();
      int bLikes = bValue == null ? 0 : bValue.intValue();
      return aLikes - bLikes;
    });
  }

  @Test
  public void getCustomActivities() throws Exception {
    Client client = Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();

    FlatFeed feed = client.flatFeed("flat", "333");

    VolleyballMatch volley = new VolleyballMatch();
    volley.actor = "Me";
    volley.object = "Message";
    volley.verb = "verb";
    volley.setNrOfBlocked(1);
    volley.setNrOfServed(1);
    feed.addCustomActivity(volley).join();

    FootballMatch football = new FootballMatch();
    football.actor = "Me";
    football.object = "Message";
    football.verb = "verb";

    football.setNrOfPenalty(2);
    football.setNrOfScore(3);
    feed.addCustomActivity(football).join();
    List<Match> result = feed.getCustomActivities(Match.class).join();
  }

  @Test(expected = CompletionException.class)
  public void invalidFeedType() throws Exception {
    Client client = Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();

    FlatFeed feed = client.flatFeed("aggregated", "1");
    List<Activity> result = feed.getActivities().join();
  }
}
