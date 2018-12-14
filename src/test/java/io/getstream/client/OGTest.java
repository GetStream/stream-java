package io.getstream.client;

import io.getstream.core.models.OGData;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OGTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void openGraph() {
        OGData[] result = new OGData[1];
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .build();

            result[0] = client.openGraph(new URL("https://joshuabloodwolf.bandcamp.com/track/thorns-run-red")).join();
        });
    }
}