package io.getstream.client;

import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.NotificationGroup;
import java.util.List;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class NotificationFeedTest {
  private static final String apiKey = System.getenv("STREAM_KEY") != null ? System.getenv("STREAM_KEY")
      : System.getProperty("STREAM_KEY");
  private static final String secret = System.getenv("STREAM_SECRET") != null ? System.getenv("STREAM_SECRET")
      : System.getProperty("STREAM_SECRET");

  @Test
  public void getActivityGroups() throws Exception {
    Client client = Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();

    NotificationFeed feed = client.notificationFeed("notification", "1");
    List<NotificationGroup<Activity>> result = feed.getActivities().join();
  }

  @Test
  public void getEnrichedActivityGroups() throws Exception {
    Client client = Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();

    NotificationFeed feed = client.notificationFeed("notification", "1");
    List<NotificationGroup<EnrichedActivity>> result = feed.getEnrichedActivities().join();
  }
}
