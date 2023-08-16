package io.getstream.cloud;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.getstream.client.Client;
import io.getstream.client.PersonalizationClient;
import io.getstream.core.models.Activity;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;

public class CloudPersonalizationClientTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");

  // personalization unsupported at the moment
  @Test
  @Ignore
  public void get() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Activity activity =
        Activity.builder()
            .actor("test")
            .verb("test")
            .object("test")
            .foreignID("picture:3")
            .extraField(
                "image",
                "https://images.unsplash.com/photo-1503088414719-16a89b27b122?auto=format&fit=crop&w=3400&q=80&ixid=dW5zcGxhc2guY29tOzs7Ozs%3D")
            .extraField("message", "Surfing at LA's beautiful Venice Beach")
            .extraField("location", "Venice Beach, LA")
            .extraField("tags", Lists.newArrayList("beach", "van", "surfing", "travel"))
            .build();
    client.flatFeed("flat", "3").addActivity(activity).join();

    Map<String, Object> result =
        client
            .personalization()
            .get("analyze_features", ImmutableMap.of("foreign_id", "picture:3"))
            .join();
  }

  @Test
  public void post() throws Exception {
    PersonalizationClient client = Client.builder(apiKey, secret).build().personalization();

    client.post("dummy_post", ImmutableMap.of("follows", "test")).join();
  }

  // XXX: disabled due to not actually having personalization endpoints supporting
  // delete calls
  @Test
  @Ignore
  public void delete() throws Exception {
    PersonalizationClient client = Client.builder(apiKey, secret).build().personalization();

    client.delete("dummy_post").join();
  }
}
