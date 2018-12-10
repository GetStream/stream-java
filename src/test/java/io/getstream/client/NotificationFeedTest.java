package io.getstream.client;

import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.NotificationGroup;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NotificationFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void getActivityGroups() {
        List<NotificationGroup<Activity>>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            NotificationFeed feed = client.notificationFeed("notification", "1");
            result[0] = feed.getActivities().join();
        });
    }

    @Test
    void getEnrichedActivityGroups() {
        List<NotificationGroup<EnrichedActivity>>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            NotificationFeed feed = client.notificationFeed("notification", "1");
            result[0] = feed.getEnrichedActivities().join();
        });
    }
}
