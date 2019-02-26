package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.client.entities.FootballMatch;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.Data;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.Reaction;
import io.getstream.core.options.EnrichmentFlags;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.CompletionException;

import static io.getstream.core.utils.Enrichment.createCollectionReference;
import static io.getstream.core.utils.Enrichment.createUserReference;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CloudFlatFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";
    private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e2";
    private static final Token token = buildToken();
    private static String actorID;

    private static Token buildToken() {
        try {
            return Client.builder(apiKey, secret).build().frontendToken(userID);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @BeforeAll
    static void setup() {
        try {
            actorID = createUserReference(Client.builder(apiKey, secret)
                    .build()
                    .user(userID)
                    .getOrCreate()
                    .join()
                    .getID());
        } catch (StreamException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getActivities() {
        List<Activity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed = client.flatFeed("flat", userID);
            result[0] = feed.getActivities().join();
        });
    }

    @Test
    void getEnrichedActivities() {
        List<EnrichedActivity>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            Data user = client.user(userID).get().join();
            CloudFlatFeed feed = client.flatFeed("rich", userID);
            Activity activity = feed.addActivity(Activity.builder()
                    .actor(actorID)
                    .verb("found")
                    .object(createCollectionReference("source-of-richness", "wealth"))
                    .build()).join();

            Reaction reaction = client.reactions().add(user.getID(), "like", activity.getID()).join();
            client.reactions().addChild(user.getID(), "like", reaction.getId()).join();

            result[0] = feed.getEnrichedActivities(new EnrichmentFlags()
                    .withOwnChildren()
                    .withUserReactions("some-user")
                    .withReactionCounts()
                    .withRecentReactions()).join();
        });
    }

    @Test
    void getCustomActivities() {
        List<Match>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed = client.flatFeed("custom", userID);

            VolleyballMatch volley = new VolleyballMatch();
            volley.actor = actorID;
            volley.object = "Message";
            volley.verb = "verb";
            volley.setNrOfBlocked(1);
            volley.setNrOfServed(1);
            feed.addCustomActivity(volley).join();

            FootballMatch football = new FootballMatch();
            football.actor = actorID;
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
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudFlatFeed feed = client.flatFeed("aggregated", userID);
            result[0] = feed.getActivities().join();
        });
    }
}
