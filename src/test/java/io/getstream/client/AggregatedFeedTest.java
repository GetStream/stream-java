package io.getstream.client;

import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.Group;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AggregatedFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void getActivityGroups() {
        List<? extends Group<Activity>>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            AggregatedFeed feed = client.aggregatedFeed("aggregated", "1");
            result[0] = feed.getActivities().join();
        });
    }

    @Test
    void getEnrichedActivityGroups() {
        List<? extends Group<EnrichedActivity>>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            AggregatedFeed feed = client.aggregatedFeed("aggregated", "1");
            result[0] = feed.getEnrichedActivities().join();
        });
    }

    @Test
    void getCustomActivityGroups() {
        List<? extends Group<Match>>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

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
            result[0] = feed.getCustomActivities(Match.class).join();
        });
    }

    @Test
    void invalidFeedType() {
        List<? extends Group<Activity>>[] result = new List[1];
        assertThrows(CompletionException.class, () -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                    .build();

            AggregatedFeed feed = client.aggregatedFeed("flat", "1");
            result[0] = feed.getActivities().join();
        });
    }
}
