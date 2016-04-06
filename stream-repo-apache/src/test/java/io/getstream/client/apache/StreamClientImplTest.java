package io.getstream.client.apache;

import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.config.StreamRegion;
import io.getstream.client.exception.InvalidFeedNameException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class StreamClientImplTest {

    private static final String API_KEY = "apikey";
    private static final String API_SECRET = "secretkey";

    private final StreamClientImpl streamClient;

    public StreamClientImplTest() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        streamClient = new StreamClientImpl(clientConfiguration, API_KEY, API_SECRET);
    }

    @Test
    public void shouldCreateClientWithDifferentRegion() throws Exception {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(StreamRegion.LOCAL_TEST),
                API_KEY, API_SECRET);
        assertThat(streamClient, notNullValue());
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