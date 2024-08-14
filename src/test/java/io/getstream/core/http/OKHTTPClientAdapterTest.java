package io.getstream.core.http;

import io.getstream.client.Client;
import io.getstream.client.FlatFeed;
import io.getstream.core.models.Activity;
import java.util.List;

import io.getstream.core.options.Limit;
import io.getstream.core.options.Offset;
import java8.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class OKHTTPClientAdapterTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");

  @Test
  public void clientCreation() throws Exception {
    Client.builder(apiKey, secret).httpClient(new OKHTTPClientAdapter(new OkHttpClient())).build();
  }


  @Test
  public void getRequest() throws Exception {
    Client client =
            Client.builder(apiKey, secret)
                    .httpClient(new OKHTTPClientAdapter())
                    .build();

    FlatFeed feed = client.flatFeed("flat", "1");

    long startTime = System.currentTimeMillis();
    List<Activity> result1 = feed.getActivities().join();
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    System.out.println("First request time: " + duration + " ms");

    for (int i=0; i<10; i++){
      startTime = System.currentTimeMillis();
      result1 = feed.getActivities().join();
      endTime = System.currentTimeMillis();
      duration = endTime - startTime;
      System.out.println("Request time: " + duration + " ms");
    }
  }

//  First request time: 868 ms
//  Request time: 124 ms
//  Request time: 135 ms
//  Request time: 138 ms
//  Request time: 126 ms
//  Request time: 133 ms
//  Request time: 128 ms
//  Request time: 130 ms
//  Request time: 130 ms
//  Request time: 126 ms
//  Request time: 128 ms

  @Test
  public void postRequest() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    Activity activity = Activity.builder().actor("test").verb("test").object("test").build();
    FlatFeed feed = client.flatFeed("flat", "1");
    Activity result = feed.addActivity(activity).join();
  }

  @Test
  public void deleteRequest() throws Exception {
    Client client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();

    FlatFeed feed = client.flatFeed("flat", "1");
    feed.removeActivityByID("654e333e-d146-11e8-bd18-1231d51167b4").join();
  }
}
