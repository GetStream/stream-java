package io.getstream.client;

import io.getstream.core.Region;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Request;
import io.getstream.core.http.Response;
import io.getstream.core.options.Pagination;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class MockHTTPClient extends HTTPClient {
    public Request lastRequest;

    @Override
    public <T> T getImplementation() { return null; }

    @Override
    public CompletableFuture<Response> execute(Request request) {
        lastRequest = request;
        Response response = new Response(200, null);
        CompletableFuture<Response> future = new CompletableFuture<>();
        future.complete(response);
        return future;
    }
}

public class ClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void clientCreation() {
        assertDoesNotThrow(() -> {
            Client.builder(apiKey, secret).httpClient(new MockHTTPClient()).build();
        });
    }

    @Test
    void regionURL() throws MalformedURLException {
        for (Region region : Region.values()) {
            MockHTTPClient httpClient = new MockHTTPClient();
            assertDoesNotThrow(() -> {
                Client client = Client.builder(apiKey, secret)
                        .region(region)
                        .httpClient(httpClient)
                        .build();
                FlatFeed feed = client.flatFeed("flat", "1");
                feed.getActivities();
            });

            assertNotNull(httpClient.lastRequest);
            URL feedURL = new URL("https://" + region + ".stream-io-api.com:443/api/v1.0/feed/flat/1/?api_key=" + apiKey + "&limit=25");
            assertEquals(httpClient.lastRequest.getURL(), feedURL);
            assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
            assertNull(httpClient.lastRequest.getBody());
        }
    }

    @Test
    void customURL() throws MalformedURLException {
        MockHTTPClient httpClient = new MockHTTPClient();
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret)
                    .httpClient(httpClient)
                    .scheme("http")
                    .host("my.test.host.com")
                    .port(1234)
                    .build();
            FlatFeed feed = client.flatFeed("flat", "1");
            feed.getActivities();
        });

        assertNotNull(httpClient.lastRequest);
        URL feedURL = new URL("http://my.test.host.com:1234/api/v1.0/feed/flat/1/?api_key=" + apiKey + "&limit=25");
        assertEquals(httpClient.lastRequest.getURL(), feedURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }

    @Test
    void feedURL() throws MalformedURLException {
        MockHTTPClient httpClient = new MockHTTPClient();
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).httpClient(httpClient).build();
            FlatFeed feed = client.flatFeed("flat", "1");
            feed.getActivities(new Pagination().offset(13).limit(69));
        });

        assertNotNull(httpClient.lastRequest);
        URL feedURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/feed/flat/1/?api_key=" + apiKey + "&offset=13&limit=69");
        assertEquals(httpClient.lastRequest.getURL(), feedURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }

    @Test
    void feedFollowURL() throws MalformedURLException {
        MockHTTPClient httpClient = new MockHTTPClient();
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).httpClient(httpClient).build();
            FlatFeed feed = client.flatFeed("flat", "1");
            feed.getFollowers();
        });

        assertNotNull(httpClient.lastRequest);
        URL followersURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/feed/flat/1/followers/?api_key=" + apiKey + "&limit=25");
        assertEquals(httpClient.lastRequest.getURL(), followersURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }

    @Test
    void feedFollowedURL() throws MalformedURLException {
        MockHTTPClient httpClient = new MockHTTPClient();
        assertDoesNotThrow(() -> {
            Client client = Client.builder(apiKey, secret).httpClient(httpClient).build();

            FlatFeed feed = client.flatFeed("flat", "1");
            feed.getFollowed();
        });

        assertNotNull(httpClient.lastRequest);
        URL followersURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/feed/flat/1/following/?api_key=" + apiKey + "&limit=25");
        assertEquals(httpClient.lastRequest.getURL(), followersURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }
}
