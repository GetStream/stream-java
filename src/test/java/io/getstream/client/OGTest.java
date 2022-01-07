package io.getstream.client;

import io.getstream.core.models.OGData;
import java.net.URL;
import org.junit.Test;

public class OGTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");

  @Test
  public void openGraph() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    OGData result = client.openGraph(new URL("https://google.com")).join();
  }
}
