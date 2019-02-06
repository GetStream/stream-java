package io.getstream.client;

import io.getstream.core.LookupKind;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.Reaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ReactionsClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void get() {
        Reaction[] result = new Reaction[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Reaction data = Reaction.builder()
                    .activityID("ed2837a6-0a3b-4679-adc1-778a1704852d")
                    .kind("like")
                    .build();
            Reaction reply = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
            result[0] = client.reactions().get(reply.getId()).join();
        });
    }

    @Test
    void filter() {
        List<Reaction>[] result = new List[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Activity activity = client.flatFeed("flat", "reactor").addActivity(Activity.builder()
                    .actor("this")
                    .verb("done")
                    .object("that")
                    .build()).join();

            client.reactions().add("john-doe", "like", activity.getID()).join();

            client.reactions().filter(LookupKind.ACTIVITY, activity.getID()).join();

            result[0] = client.reactions().filter(LookupKind.ACTIVITY_WITH_DATA, activity.getID(), "comment").join();
        });
    }

    @Test
    void add() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Reaction data = Reaction.builder()
                    .activityID("ed2837a6-0a3b-4679-adc1-778a1704852d")
                    .kind("like")
                    .build();
            client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
        });
    }

    @Test
    void addChild() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Reaction data = Reaction.builder()
                    .activityID("ed2837a6-0a3b-4679-adc1-778a1704852d")
                    .kind("like")
                    .build();
            data = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
            Reaction child = client.reactions().addChild("user-id", "like", data.getId()).join();
        });
    }

    @Test
    void update() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Reaction data = Reaction.builder()
                    .id("b5c46f9b-0839-4207-86aa-6a7f388b7748")
                    .kind("like")
                    .extraField("key", "value")
                    .build();
            client.reactions().update(data, new FeedID("flat", "1")).join();
        });
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Reaction data = Reaction.builder()
                    .activityID("ed2837a6-0a3b-4679-adc1-778a1704852d")
                    .kind("like")
                    .build();
            Reaction reply = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
            client.reactions().delete(reply.getId()).join();
        });
    }
}