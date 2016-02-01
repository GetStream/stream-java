package io.getstream.client.okhttp;

import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.InvalidFeedNameException;
import org.junit.Test;

public class StreamClientImplTest {

    private static final String API_KEY = "apikey";
    private static final String API_SECRET = "secretkey";

    private final StreamClientImpl streamClient;

    public StreamClientImplTest() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        streamClient = new StreamClientImpl(clientConfiguration, API_KEY, API_SECRET);
    }

    @Test
    public void shouldCreateNewFeed() throws Exception {
        streamClient.newFeed("foo", "1");
    }

    @Test(expected = InvalidFeedNameException.class)
    public void shouldFailCreatingNewFeed() throws Exception {
        streamClient.newFeed("foo+bar", "1");
    }

    @Test(expected = InvalidFeedNameException.class)
    public void shouldFailCreatingNewFeedAgain() throws Exception {
        streamClient.newFeed("foo", "1+bar");
    }

    @Test
    public void shouldShutdown() throws Exception {
        streamClient.shutdown();
    }
}