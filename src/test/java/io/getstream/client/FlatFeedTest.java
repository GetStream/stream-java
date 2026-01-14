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
import java.util.concurrent.atomic.AtomicReference;

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

  @Test
  public void testGetActivitiesUrlParameters() throws Exception {
    // Create a mock HTTP client that captures the request URL
    AtomicReference<String> capturedUrl = new AtomicReference<>();
    
    // Create a custom OkHttpClient that intercepts requests
    OkHttpClient mockClient = new OkHttpClient.Builder()
        .addInterceptor(chain -> {
          capturedUrl.set(chain.request().url().toString());
          // Return a mock response
          return new okhttp3.Response.Builder()
              .request(chain.request())
              .protocol(okhttp3.Protocol.HTTP_1_1)
              .code(200)
              .message("OK")
              .body(okhttp3.ResponseBody.create(
                  okhttp3.MediaType.parse("application/json"),
                  "{\"results\":[],\"next\":\"\",\"duration\":\"0ms\"}"
              ))
              .build();
        })
        .build();

    Client client = Client.builder("test-key", "test-secret")
        .httpClient(new OKHTTPClientAdapter(mockClient))
        .build();

    FlatFeed feed = client.flatFeed("flat", "test-url-params");
    
    // Test with multiple RequestOptions
    feed.getActivities(
        new Limit(20), 
        new Offset(5), 
        new Filter().refresh(), 
        new DiscardActors("actor1", "actor2", "actor3")
    ).join();
    
    String url = capturedUrl.get();
    assert url != null;
    
    // Verify URL contains expected parameters
    assert url.contains("limit=20") : "URL should contain limit=20, got: " + url;
    assert url.contains("offset=5") : "URL should contain offset=5, got: " + url;
    assert url.contains("refresh=1") : "URL should contain refresh=1, got: " + url;
    assert url.contains("discard_actors=actor1,actor2,actor3") : "URL should contain discard_actors, got: " + url;
    
    // Test with custom separator
    capturedUrl.set(null);
    feed.getActivities(
        new DiscardActors(new String[]{"actor4", "actor5"}, "|")
    ).join();
    
    url = capturedUrl.get();
    assert url != null;
    assert url.contains("discard_actors=actor4%7Cactor5") : "URL should contain pipe-separated actors, got: " + url;
    assert url.contains("discard_actors_sep=%7C") : "URL should contain discard_actors_sep, got: " + url;
  }

  @Test
  public void testGetEnrichedActivitiesWithDiscardActors() throws Exception {
    if (apiKey == null || secret == null) {
      System.out.println("Skipping testGetEnrichedActivitiesWithDiscardActors - API credentials not set");
      return;
    }
    
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "test-enriched-discard");
    
    // Test with just DiscardActors on enriched activities
    DiscardActors discardActors = new DiscardActors("actor1", "actor2", "actor3");
    List<EnrichedActivity> result1 = feed.getEnrichedActivities(discardActors).join();
    assert result1 != null;
    
    // Test with DiscardActors + Limit + EnrichmentFlags
    List<String> actors = java.util.Arrays.asList("actor4", "actor5");
    DiscardActors discardActors2 = new DiscardActors(actors);
    EnrichmentFlags flags = new EnrichmentFlags().withOwnReactions().withReactionCounts();
    List<EnrichedActivity> result2 = feed.getEnrichedActivities(
        new Limit(10), 
        flags, 
        discardActors2
    ).join();
    assert result2 != null;
    
    // Test with all parameters including custom separator
    List<EnrichedActivity> result3 = feed.getEnrichedActivities(
        new Limit(20), 
        new Offset(5), 
        new Filter().refresh(), 
        new EnrichmentFlags().withRecentReactions(),
        new DiscardActors(new String[]{"actor6", "actor7"}, "|")
    ).join();
    assert result3 != null;
  }

  @Test
  public void testGetEnrichedActivitiesUrlParameters() throws Exception {
    // Create a mock HTTP client that captures the request URL
    AtomicReference<String> capturedUrl = new AtomicReference<>();
    
    // Create a custom OkHttpClient that intercepts requests
    OkHttpClient mockClient = new OkHttpClient.Builder()
        .addInterceptor(chain -> {
          capturedUrl.set(chain.request().url().toString());
          // Return a mock response for enriched activities
          return new okhttp3.Response.Builder()
              .request(chain.request())
              .protocol(okhttp3.Protocol.HTTP_1_1)
              .code(200)
              .message("OK")
              .body(okhttp3.ResponseBody.create(
                  okhttp3.MediaType.parse("application/json"),
                  "{\"results\":[],\"next\":\"\",\"duration\":\"0ms\"}"
              ))
              .build();
        })
        .build();

    Client client = Client.builder("test-key", "test-secret")
        .httpClient(new OKHTTPClientAdapter(mockClient))
        .build();

    FlatFeed feed = client.flatFeed("flat", "test-enriched-url");
    
    // Test enriched activities with discard_actors
    feed.getEnrichedActivities(
        new Limit(15), 
        new EnrichmentFlags().withOwnReactions(),
        new DiscardActors("user1", "user2")
    ).join();
    
    String url = capturedUrl.get();
    assert url != null;
    
    // Verify URL contains expected parameters
    assert url.contains("limit=15") : "URL should contain limit=15, got: " + url;
    assert url.contains("discard_actors=user1,user2") : "URL should contain discard_actors, got: " + url;
    assert url.contains("with_own_reactions=true") : "URL should contain enrichment flags, got: " + url;
    
    // Test with custom separator on enriched activities
    capturedUrl.set(null);
    feed.getEnrichedActivities(
        new DiscardActors(new String[]{"userA", "userB", "userC"}, ";")
    ).join();
    
    url = capturedUrl.get();
    assert url != null;
    // Note: The semicolon may or may not be URL-encoded depending on the HTTP client
    assert (url.contains("discard_actors=userA%3BuserB%3BuserC") || url.contains("discard_actors=userA;userB;userC")) 
        : "URL should contain semicolon-separated actors, got: " + url;
    assert (url.contains("discard_actors_sep=%3B") || url.contains("discard_actors_sep=;")) 
        : "URL should contain discard_actors_sep, got: " + url;
  }
}
