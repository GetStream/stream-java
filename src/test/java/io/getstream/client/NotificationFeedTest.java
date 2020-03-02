package io.getstream.client;

import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.NotificationGroup;
import java.util.List;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class NotificationFeedTest {
  private static final String apiKey = "gp6e8sxxzud6";
  private static final String secret =
      "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

  @Test
  public void getActivityGroups() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    NotificationFeed feed = client.notificationFeed("notification", "1");
    List<NotificationGroup<Activity>> result = feed.getActivities().join();
  }

  @Test
  public void getEnrichedActivityGroups() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    NotificationFeed feed = client.notificationFeed("notification", "1");
    List<NotificationGroup<EnrichedActivity>> result = feed.getEnrichedActivities().join();
  }
}
