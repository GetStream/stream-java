package io.getstream.client;

import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Activity;
import io.getstream.core.models.Data;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.Reaction;
import io.getstream.core.options.EnrichmentFlags;
import java8.util.concurrent.CompletionException;
import okhttp3.OkHttpClient;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static io.getstream.core.utils.Enrichment.createCollectionReference;
import static io.getstream.core.utils.Enrichment.createUserReference;

public class FlatFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    public void getActivities() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build();

        int takeId = 1;
        FlatFeed feed = client.flatFeed("flat", "1");
        List<Activity> result = feed.getActivities().join();
    }

    @Test
    public void getEnrichedActivities() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build();

        Data user = client.user("john-doe").getOrCreate(new Data().set("hey", "now")).join();
        FlatFeed feed = client.flatFeed("flat", "rich");

        List<EnrichedActivity> result = feed.getEnrichedActivities(new EnrichmentFlags()
                .withOwnChildren()
                .withUserReactions("some-user")
                .withReactionCounts()
                .withRecentReactions()).join();
        result.sort((a, b) -> {
            int aLikes = a.getReactionCounts().getOrDefault("like", 0).intValue();
            int bLikes = b.getReactionCounts().getOrDefault("like", 0).intValue();
            return aLikes - bLikes;
        });
    }

    @Test
    public void getCustomActivities() throws Exception {
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
        List<Match> result = feed.getCustomActivities(Match.class).join();
    }

    @Test(expected = CompletionException.class)
    public void invalidFeedType() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build();

        FlatFeed feed = client.flatFeed("aggregated", "1");
        List<Activity> result = feed.getActivities().join();
    }
}
