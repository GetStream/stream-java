package io.getstream.client;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.FollowStats;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class FeedTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");

  @Test
  public void addActivity() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    Activity activity = Activity.builder().actor("test").verb("test").object("test").build();
    FlatFeed feed = client.flatFeed("flat", "1");
    Activity result = feed.addActivity(activity).join();
  }

  @Test
  public void addActivities() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    Activity activity = Activity.builder().actor("test").verb("test").object("test").build();
    FlatFeed feed = client.flatFeed("flat", "1");
    List<Activity> result = feed.addActivities(activity).join();
  }

  @Test
  public void addCustomActivities() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    VolleyballMatch volley = new VolleyballMatch();
    volley.actor = "Me";
    volley.object = "Message";
    volley.verb = "verb";
    volley.setNrOfBlocked(1);
    volley.setNrOfServed(1);

    FootballMatch football = new FootballMatch();
    football.actor = "Me";
    football.object = "Message";
    football.verb = "verb";

    football.setNrOfPenalty(2);
    football.setNrOfScore(3);

    FlatFeed feed = client.flatFeed("flat", "1");
    List<Match> result = feed.addCustomActivities(volley, football).join();
  }

  @Test
  public void removeActivityByID() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "1");
    feed.removeActivityByID("654e333e-d146-11e8-bd18-1231d51167b4").join();
  }

  @Test
  public void removeActivityByForeignID() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "1");
    feed.removeActivityByForeignID("654e333e-d146-11e8-bd18-1231d51167b4").join();
  }

  @Test
  public void follow() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed1 = client.flatFeed("flat", "1");
    FlatFeed feed2 = client.flatFeed("flat", "2");
    feed1.follow(feed2).join();
  }

  @Test
  public void getFollowers() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "1");
    feed.getFollowers().join();
  }

  @Test
  public void getFollowed() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "1");
    feed.getFollowed().join();
  }

  @Test
  public void unfollow() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed1 = client.flatFeed("flat", "1");
    FlatFeed feed2 = client.flatFeed("flat", "2");
    feed1.unfollow(feed2).join();
  }

  @Test
  public void getFollowStats() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();
    String uuid1 = UUID.randomUUID().toString().replace("-", "");
    String uuid2 = UUID.randomUUID().toString().replace("-", "");
    String feed1Id = "flat:" + uuid1;
    FlatFeed feed1 = client.flatFeed("flat", uuid1);
    FlatFeed feed2 = client.flatFeed("flat", uuid2);
    feed1.follow(feed2).join();

    FollowStats stats =
        feed1.getFollowStats(Collections.emptyList(), Lists.newArrayList("timeline")).join();
    assertEquals(0, stats.getFollowers().getCount());
    assertEquals(feed1Id, stats.getFollowers().getFeed());
    assertEquals(0, stats.getFollowing().getCount());
    assertEquals(feed1Id, stats.getFollowing().getFeed());

    stats = feed1.getFollowStats(Collections.emptyList(), Lists.newArrayList("flat")).join();
    assertEquals(0, stats.getFollowers().getCount());
    assertEquals(feed1Id, stats.getFollowers().getFeed());
    assertEquals(1, stats.getFollowing().getCount());
    assertEquals(feed1Id, stats.getFollowing().getFeed());
  }

  @Test
  public void updateActivityToTargets() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "bob");
    Activity activity =
        Activity.builder()
            .actor("shmest")
            .verb("test")
            .object("test")
            .foreignID("foreignID-1-2-3-4")
            .time(new Date())
            .to(Lists.newArrayList(new FeedID("flat:alice")))
            .build();
    Activity result = feed.addActivity(activity).join();
    feed.updateActivityToTargets(
        result, new FeedID[] {new FeedID("flat", "claire")}, new FeedID[] {});
    List<Activity> after = feed.getActivities().join();
  }

  @Test
  public void replaceActivityToTargets() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "1");
    Activity activity =
        Activity.builder()
            .actor("test")
            .verb("test")
            .object("test")
            .foreignID("foreignID")
            .time(new Date())
            .to(Lists.newArrayList(new FeedID("feed:2")))
            .build();
    feed.replaceActivityToTargets(activity, new FeedID("feed:3"));
  }
}
