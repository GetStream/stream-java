package io.getstream.client;

import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.Data;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.options.DiscardActors;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Limit;
import io.getstream.core.options.Offset;
import java.util.Collections;
import java.util.List;
import java8.util.concurrent.CompletionException;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class FlatFeedTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");

  @Test
  public void getActivities() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    int takeId = 1;
    FlatFeed feed = client.flatFeed("flat", "1");
    List<Activity> result = feed.getActivities().join();
  }

  @Test
  public void getEnrichedActivities() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    Data user = client.user("john-doe").getOrCreate(new Data().set("hey", "now")).join();
    FlatFeed feed = client.flatFeed("flat", "rich");

    List<EnrichedActivity> result =
        feed.getEnrichedActivities(
                new EnrichmentFlags()
                    .withOwnChildren()
                    .withUserReactions("some-user")
                    .withReactionCounts()
                    .withRecentReactions())
            .join();
    Collections.sort(
        result,
        (a, b) -> {
          Number aValue = a.getReactionCounts().get("like");
          Number bValue = b.getReactionCounts().get("like");
          int aLikes = aValue == null ? 0 : aValue.intValue();
          int bLikes = bValue == null ? 0 : bValue.intValue();
          return aLikes - bLikes;
        });
  }

  @Test
  public void getCustomActivities() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

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
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("aggregated", "1");
    feed.getActivities().join();
  }


  @Test
  public void testDiscardActorsOptions() {
    // Test DiscardActors with array
    DiscardActors discardActors1 = new DiscardActors("user1", "user2", "user3");
    
    // Test DiscardActors with List
    List<String> actors = java.util.Arrays.asList("user4", "user5");
    DiscardActors discardActors2 = new DiscardActors(actors);
    
    // Test DiscardActors with custom separator
    DiscardActors discardActors3 = new DiscardActors(new String[]{"user6", "user7"}, ";");
    
    // Test DiscardActors with List and custom separator
    DiscardActors discardActors4 = new DiscardActors(actors, "|");
    
    // Basic validation that objects were created
    assert discardActors1 != null;
    assert discardActors2 != null;
    assert discardActors3 != null;
    assert discardActors4 != null;
  }

  @Test
  public void testGetActivitiesWithRequestOptions() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "test-request-options");
    
    // Test with just DiscardActors
    DiscardActors discardActors = new DiscardActors("actor1", "actor2", "actor3");
    List<Activity> result1 = feed.getActivities(discardActors).join();
    assert result1 != null;
    
    // Test with DiscardActors + Limit + Filter
    List<String> actors = java.util.Arrays.asList("actor4", "actor5");
    DiscardActors discardActors2 = new DiscardActors(actors);
    Filter filter = new Filter().refresh();
    List<Activity> result2 = feed.getActivities(new Limit(10), filter, discardActors2).join();
    assert result2 != null;
    
    // Test with all parameters
    List<Activity> result3 = feed.getActivities(
        new Limit(20), 
        new Offset(5), 
        new Filter().refresh(), 
        new DiscardActors("actor6", "actor7")
    ).join();
    assert result3 != null;
  }
}
