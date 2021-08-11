package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.core.http.Token;
import io.getstream.core.models.Data;
import io.getstream.core.models.ProfileData;
import java.net.MalformedURLException;
import org.junit.Before;
import org.junit.Test;

public class CloudUserTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");
  private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e2";
  private static final Token token = buildToken();

  private static Token buildToken() {
    try {
      return Client.builder(apiKey, secret).build().frontendToken(userID);
    } catch (MalformedURLException e) {
      return null;
    }
  }

  @Before
  public void setup() throws Exception {
    Client.builder(apiKey, secret).build().user(userID).getOrCreate().join();
  }

  @Test
  public void get() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudUser user = client.user(userID);
    Data result = user.get().join();
  }

  @Test
  public void delete() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudUser user = client.user(userID);
    user.delete().join();
  }

  @Test
  public void getOrCreate() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudUser user = client.user(userID);
    user.delete().join();
    Data result = user.getOrCreate().join();
  }

  @Test
  public void create() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudUser user = client.user(userID);
    user.delete().join();
    Data result = user.create().join();
  }

  @Test
  public void update() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudUser user = client.user(userID);
    Data result = user.update(new Data().set("key", "value").set("null", null)).join();
  }

  @Test
  public void profile() throws Exception {
    CloudClient client = CloudClient.builder(apiKey, token, userID).build();

    CloudUser user = client.user(userID);
    ProfileData result = user.profile().join();
  }
}
