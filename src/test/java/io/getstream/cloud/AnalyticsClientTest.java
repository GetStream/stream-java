package io.getstream.cloud;

import com.google.common.collect.Lists;
import io.getstream.client.AnalyticsClient;
import io.getstream.client.Client;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.Content;
import io.getstream.core.models.Engagement;
import io.getstream.core.models.Impression;
import io.getstream.core.models.UserData;
import okhttp3.OkHttpClient;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class AnalyticsClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    public void trackEngagement() throws Exception {
        AnalyticsClient client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build()
                .analytics();

        client.trackEngagement(Engagement.builder()
                .userData(new UserData("test", "test"))
                .label("click")
                .content(new Content("tweet:34349698"))
                .boost(2)
                .position(3)
                .feedID("user:thierry")
                .location("profile_page")
                .build()).join();
    }

    @Test
    public void trackImpression() throws Exception {
        AnalyticsClient client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build()
                .analytics();

        List<Content> content = Lists.newArrayList(new Content("tweet:34349698"), new Content("tweet:34349699"), new Content("tweet:34349697"));
        client.trackImpression(Impression.builder()
                .userData(new UserData("test", "test"))
                .contentList(content)
                .feedID("flat:tommaso")
                .location("profile_page")
                .build()).join();
    }

    @Test
    public void createRedirectURL() throws Exception {
        AnalyticsClient client = Client.builder(apiKey, secret)
                .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
                .build()
                .analytics();

        Engagement engagement = Engagement.builder()
                .userData(new UserData("test", "test"))
                .label("click")
                .content(new Content("tweet:34349698"))
                .boost(2)
                .position(3)
                .feedID("user:thierry")
                .location("profile_page")
                .build();

        List<Content> content = Lists.newArrayList(new Content("tweet:34349698"), new Content("tweet:34349699"), new Content("tweet:34349697"));
        Impression impression = Impression.builder()
                .userData(new UserData("test", "test"))
                .contentList(content)
                .feedID("flat:tommaso")
                .location("profile_page")
                .build();

        client.createRedirectURL(new URL("https://getstream.io"), Lists.newArrayList(impression), Lists.newArrayList(engagement));
    }
}