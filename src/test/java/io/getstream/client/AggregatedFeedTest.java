package io.getstream.client;

import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.Group;
import io.getstream.core.options.EnrichmentFlags;
import java8.util.concurrent.CompletionException;
import okhttp3.OkHttpClient;
import org.junit.Test;

import java.util.List;

public class AggregatedFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    public void getActivityGroups() throws Exception {
        Client client = Client.builder(apiKey, secret).build();

        AggregatedFeed feed = client.aggregatedFeed("aggregated", "1");
        List<? extends Group<Activity>> result = feed.getActivities().join();
    }

    @Test
    public void getEnrichedActivityGroups() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build();

        AggregatedFeed feed = client.aggregatedFeed("aggregated", "1");
        List<? extends Group<EnrichedActivity>> result = feed.getEnrichedActivities(new EnrichmentFlags()
                        .withReactionCounts()
                        .withRecentReactions()
                        .withOwnReactions())
                .join();
    }

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
