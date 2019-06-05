package io.getstream.client;

import io.getstream.core.Region;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Request;
import io.getstream.core.http.Response;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Limit;
import io.getstream.core.options.Offset;
import java8.util.concurrent.CompletableFuture;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

class MockHTTPClient extends HTTPClient {
    public Request lastRequest;

    @Override
    public <T> T getImplementation() {
        return null;
    }

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
    public void clientCreation() throws Exception {
        Client.builder(apiKey, secret).httpClient(new MockHTTPClient()).build();
    }

    @Test
    public void regionURL() throws Exception {
        for (Region region : Region.values()) {
            MockHTTPClient httpClient = new MockHTTPClient();
            Client client = Client.builder(apiKey, secret)
                    .region(region)
                    .httpClient(httpClient)
                    .build();
            FlatFeed feed = client.flatFeed("flat", "1");
            feed.getActivities();

            assertNotNull(httpClient.lastRequest);
            URL feedURL = new URL("https://" + region + ".stream-io-api.com:443/api/v1.0/feed/flat/1/?api_key=" + apiKey + "&limit=25");
            assertEquals(httpClient.lastRequest.getURL(), feedURL);
            assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
            assertNull(httpClient.lastRequest.getBody());
        }
    }

    @Test
    public void customURL() throws Exception {
        MockHTTPClient httpClient = new MockHTTPClient();
        Client client = Client.builder(apiKey, secret)
                .httpClient(httpClient)
                .scheme("http")
                .host("my.test.host.com")
                .port(1234)
                .build();
        FlatFeed feed = client.flatFeed("flat", "1");
        feed.getActivities();

        assertNotNull(httpClient.lastRequest);
        URL feedURL = new URL("http://my.test.host.com:1234/api/v1.0/feed/flat/1/?api_key=" + apiKey + "&limit=25");
        assertEquals(httpClient.lastRequest.getURL(), feedURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }

    @Test
    public void feedURL() throws Exception {
        MockHTTPClient httpClient = new MockHTTPClient();
        Client client = Client.builder(apiKey, secret).httpClient(httpClient).build();
        FlatFeed feed = client.flatFeed("flat", "1");
        feed.getActivities(new Limit(69), new Offset(13));

        assertNotNull(httpClient.lastRequest);
        URL feedURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/feed/flat/1/?api_key=" + apiKey + "&limit=69&offset=13");
        assertEquals(httpClient.lastRequest.getURL(), feedURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());

        feed.getActivities(new Limit(69), new Filter().idGreaterThanEqual("123").idLessThanEqual("456"));

        assertNotNull(httpClient.lastRequest);
        feedURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/feed/flat/1/?api_key=" + apiKey + "&limit=69&id_gte=123&id_lte=456");
        assertEquals(httpClient.lastRequest.getURL(), feedURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }

    @Test
    public void enrichedFeedURL() throws Exception {
        MockHTTPClient httpClient = new MockHTTPClient();
        Client client = Client.builder(apiKey, secret).httpClient(httpClient).build();
        FlatFeed feed = client.flatFeed("flat", "1");
        feed.getEnrichedActivities(new Limit(69), new Offset(13));

        assertNotNull(httpClient.lastRequest);
        URL feedURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/enrich/feed/flat/1/?api_key=" + apiKey + "&limit=69&offset=13");
        assertEquals(httpClient.lastRequest.getURL(), feedURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());

        feed.getEnrichedActivities(new Limit(69), new Filter().idGreaterThanEqual("123").idLessThanEqual("456"), new EnrichmentFlags().withUserReactions("user1"));

        assertNotNull(httpClient.lastRequest);
        feedURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/enrich/feed/flat/1/?api_key=" + apiKey + "&limit=69&id_gte=123&id_lte=456&with_own_reactions=true&user_id=user1");
        assertEquals(httpClient.lastRequest.getURL(), feedURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }

    @Test
    public void feedFollowURL() throws Exception {
        MockHTTPClient httpClient = new MockHTTPClient();
        Client client = Client.builder(apiKey, secret).httpClient(httpClient).build();
        FlatFeed feed = client.flatFeed("flat", "1");
        feed.getFollowers();

        assertNotNull(httpClient.lastRequest);
        URL followersURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/feed/flat/1/followers/?api_key=" + apiKey + "&limit=25");
        assertEquals(httpClient.lastRequest.getURL(), followersURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }

    @Test
    public void feedFollowedURL() throws Exception {
        MockHTTPClient httpClient = new MockHTTPClient();
        Client client = Client.builder(apiKey, secret).httpClient(httpClient).build();

        FlatFeed feed = client.flatFeed("flat", "1");
        feed.getFollowed();

        assertNotNull(httpClient.lastRequest);
        URL followersURL = new URL("https://us-east-api.stream-io-api.com:443/api/v1.0/feed/flat/1/following/?api_key=" + apiKey + "&limit=25");
        assertEquals(httpClient.lastRequest.getURL(), followersURL);
        assertEquals(httpClient.lastRequest.getMethod(), Request.Method.GET);
        assertNull(httpClient.lastRequest.getBody());
    }
}
