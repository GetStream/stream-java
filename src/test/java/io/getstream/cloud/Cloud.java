package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.core.models.OGData;
import java.net.URL;
import org.junit.Test;

public class Cloud {
  private static final String apiKey = "gp6e8sxxzud6";
  private static final String secret =
      "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

  @Test
  public void openGraph() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    OGData result =
        client
            .openGraph(new URL("https://joshuabloodwolf.bandcamp.com/track/thorns-run-red"))
            .join();
  }
}
