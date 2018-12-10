package io.getstream.client;

import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlatFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void getActivities() {
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
    void getEnrichedActivities() {
        List<EnrichedActivity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("flat", "1");
            result[0] = feed.getEnrichedActivities().join();
        });
    }

    @Test
    void getCustomActivities() {
        List<Match>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
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
            result[0] = feed.getCustomActivities(Match.class).join();
        });
    }

    @Test
    void invalidFeedType() {
        List<Activity>[] result = new List[1];
        assertThrows(CompletionException.class, () -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            FlatFeed feed = client.flatFeed("aggregated", "1");
            result[0] = feed.getActivities().join();
        });
    }
}
