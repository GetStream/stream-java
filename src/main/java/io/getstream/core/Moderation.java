package io.getstream.core;

import static io.getstream.core.utils.Request.buildPost;
import static io.getstream.core.utils.Routes.*;
import static io.getstream.core.utils.Serialization.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Response;
import io.getstream.core.http.Token;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public class Moderation {
  private final String key;
  private final URL baseURL;
  private final HTTPClient httpClient;

  public Moderation(String key, URL baseURL, HTTPClient httpClient) {
    this.key = key;
    this.baseURL = baseURL;
    this.httpClient = httpClient;
  }

  public CompletableFuture<Response> flag(
      Token token,
      String entityType,
      String entityId,
      String reportingUser,
      String Reason,
      Map<String, Object> Custom)
      throws StreamException {
    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public final String user_id = reportingUser;
                public final String entity_type = entityType;
                public final String entity_id = entityId;
                public final String reason = Reason;
                public final Map<String, Object> custom = Custom;
              });

      final URL url = buildModerationFlagURL(baseURL);
      return httpClient.execute(buildPost(url, key, token, payload));
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new CompletionException(e);
    }
  }
}
