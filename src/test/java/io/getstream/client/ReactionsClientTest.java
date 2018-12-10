package io.getstream.client;

import io.getstream.core.models.FeedID;
import io.getstream.core.models.Reaction;
import io.getstream.core.LookupKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReactionsClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void get() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            Reaction data = Reaction.builder()
                    .activityID("ed2837a6-0a3b-4679-adc1-778a1704852d")
                    .kind("like")
                    .build();
            Reaction reply = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
            client.reactions().get(reply.getId()).join();
        });
    }

    @Test
    void filter() {
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).build();

            client.reactions().filter(LookupKind.ACTIVITY, "e28893d6-fc7f-11e8-8eb2-f2801f1b9fd1").join();
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