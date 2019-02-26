package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.client.NotificationFeed;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.NotificationGroup;
import io.getstream.core.utils.Enrichment;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CloudNotificationFeedTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";
    private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e1";
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
            actorID = Enrichment.createUserReference(Client.builder(apiKey, secret)
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
    void addActivity() {
        Activity[] result = new Activity[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            Activity activity = Activity.builder()
                    .actor(actorID)
                    .verb("test")
                    .object("test")
                    .build();
            CloudNotificationFeed feed = client.notificationFeed("notification", userID);
            result[0] = feed.addActivity(activity).join();
        });
    }

    @Test
    void getActivityGroups() {
        List<NotificationGroup<Activity>>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudNotificationFeed feed = client.notificationFeed("notification", userID);
            result[0] = feed.getActivities().join();
        });
    }

    @Test
    void getEnrichedActivityGroups() {
        List<NotificationGroup<EnrichedActivity>>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudNotificationFeed feed = client.notificationFeed("rich_notification", userID);
            result[0] = feed.getEnrichedActivities().join();
        });
    }
}
