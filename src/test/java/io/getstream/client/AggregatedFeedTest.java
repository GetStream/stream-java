package io.getstream.client;

import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.Group;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.options.Limit;
import io.getstream.core.options.Offset;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java8.util.concurrent.CompletionException;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class AggregatedFeedTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");

  @Test
  public void getActivityGroups() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    AggregatedFeed feed = client.aggregatedFeed("aggregated", "1");
    List<? extends Group<Activity>> result = feed.getActivities().join();
  }

  @Test
  public void getEnrichedActivityGroups() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    AggregatedFeed feed = client.aggregatedFeed("aggregated", "1");
    List<? extends Group<EnrichedActivity>> result =
        feed.getEnrichedActivities(
                new EnrichmentFlags().withReactionCounts().withRecentReactions().withOwnReactions())
            .join();
  }

  /*
  @Test
  public void getEnrichedRankingVars() throws Exception {
    MockHTTPClient httpClient = new MockHTTPClient();
    Client client =
        Client.builder(apiKey, secret)
        .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))//.httpClient(httpClient)
            .build();

    Map<String, Object> mp = new LinkedHashMap();

    mp.put("boolVal", true);
    mp.put("music", 1);
    mp.put("sports", 2.1);
    mp.put("string", "str");

    FlatFeed feed = client.flatFeed("flat", "123");
    
    List<EnrichedActivity> result = feed.getEnrichedActivities(
      new Limit(69),
      new Offset(13),
      new EnrichmentFlags().rankingVars(mp), "popularity").join();

    assertNotNull(result);
  }
  */

  @Test
  public void getCustomActivityGroups() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    AggregatedFeed feed = client.aggregatedFeed("aggregated", "777");

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
    List<? extends Group<Match>> result = feed.getCustomActivities(Match.class).join();
  }

  @Test(expected = CompletionException.class)
  public void invalidFeedType() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    AggregatedFeed feed = client.aggregatedFeed("flat", "1");
    List<? extends Group<Activity>> result = feed.getActivities().join();
  }
}
