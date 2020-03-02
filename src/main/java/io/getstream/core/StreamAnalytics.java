package io.getstream.core;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Request.buildPost;
import static io.getstream.core.utils.Routes.buildAnalyticsURL;
import static io.getstream.core.utils.Serialization.deserializeError;
import static io.getstream.core.utils.Serialization.toJSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import com.google.common.collect.ObjectArrays;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.models.Engagement;
import io.getstream.core.models.Impression;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public final class StreamAnalytics {
  private final String key;
  private final URL baseURL;
  private final HTTPClient httpClient;

  StreamAnalytics(String key, URL baseURL, HTTPClient httpClient) {
    this.key = key;
    this.baseURL = baseURL;
    this.httpClient = httpClient;
  }

  public CompletableFuture<Void> trackEngagement(Token token, Engagement... events)
      throws StreamException {
    checkNotNull(events, "No events to track");
    checkArgument(events.length > 0, "No events to track");

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public final Engagement[] content_list = events;
              });
      final URL url = buildAnalyticsURL(baseURL, "engagement/");
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserializeError(response);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Void> trackImpression(Token token, Impression event)
      throws StreamException {
    checkNotNull(event, "No events to track");

    try {
      final byte[] payload = toJSON(event);
      final URL url = buildAnalyticsURL(baseURL, "impression/");
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserializeError(response);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public URL createRedirectURL(
      Token token, URL url, Impression[] impressions, Engagement[] engagements)
      throws StreamException {
    try {
      final byte[] events = toJSON(ObjectArrays.concat(impressions, engagements, Object.class));
      return HTTPClient.requestBuilder()
          .url(buildAnalyticsURL(baseURL, "redirect/"))
          .addQueryParameter("api_key", key)
          .addQueryParameter("url", url.toExternalForm())
          .addQueryParameter("events", new String(events, Charsets.UTF_8))
          .addQueryParameter("auth_type", "jwt")
          .addQueryParameter("authorization", token.toString())
          .build()
          .getURL();
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }
}
