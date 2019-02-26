package io.getstream.cloud;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.getstream.client.Client;
import io.getstream.client.PersonalizationClient;
import io.getstream.core.models.Activity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CloudPersonalizationClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void get() {
        Map<String, Object>[] result = new Map[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .build();

            Activity activity = Activity.builder()
                    .actor("test")
                    .verb("test")
                    .object("test")
                    .foreignID("picture:3")
                    .extraField("image", "https://images.unsplash.com/photo-1503088414719-16a89b27b122?auto=format&fit=crop&w=3400&q=80&ixid=dW5zcGxhc2guY29tOzs7Ozs%3D")
                    .extraField("message", "Surfing at LA's beautiful Venice Beach")
                    .extraField("location", "Venice Beach, LA")
                    .extraField("tags", Lists.newArrayList("beach", "van", "surfing", "travel"))
                    .build();
            client.flatFeed("flat", "3").addActivity(activity).join();

            result[0] = client.personalization().get("analyze_features", ImmutableMap.of("foreign_id", "picture:3")).join();
        });
    }

    @Test
    void post() {
        assertDoesNotThrow(() -> {
            PersonalizationClient client = Client.builder(apiKey, secret)
                    .build()
                    .personalization();

            client.post("dummy_post", ImmutableMap.of("follows", "test")).join();
        });
    }

    //XXX: disabled due to not actually having personalization endpoints supporting delete calls
    @Test
    @Disabled
    void delete() {
        assertDoesNotThrow(() -> {
            PersonalizationClient client = Client.builder(apiKey, secret)
                    .build()
                    .personalization();

            client.delete("dummy_post").join();
        });
    }
}