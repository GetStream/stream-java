package io.getstream.core.http;

import io.getstream.client.Client;
import io.getstream.client.FlatFeed;
import io.getstream.core.models.Activity;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OKHTTPClientAdapterTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void clientCreation() {
        assertDoesNotThrow(() -> {
            Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();
        });
    }

    @Test
    void getRequest() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            result[0] = feed.getActivities().join();
        });
    }

    @Test
    void postRequest() {
        Activity[] result = new Activity[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build();

            Activity activity = Activity.builder()
                .actor("test")
                .verb("test")
                .object("test")
                .build();
            FlatFeed feed = client.flatFeed("flat", "1");
            result[0] = feed.addActivity(activity).join();
        });
    }

    @Test
    void deleteRequest() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            feed.removeActivityByID("654e333e-d146-11e8-bd18-1231d51167b4").join();
        });
    }
}
