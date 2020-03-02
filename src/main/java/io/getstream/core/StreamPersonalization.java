package io.getstream.core;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Request.*;
import static io.getstream.core.utils.Routes.buildPersonalizationURL;
import static io.getstream.core.utils.Serialization.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.options.RequestOption;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;
import java8.util.stream.StreamSupport;

public final class StreamPersonalization {
  private final String key;
  private final URL baseURL;
  private final HTTPClient httpClient;

  StreamPersonalization(String key, URL baseURL, HTTPClient httpClient) {
    this.key = key;
    this.baseURL = baseURL;
    this.httpClient = httpClient;
  }

  public CompletableFuture<Map<String, Object>> get(
      Token token, String userID, String resource, Map<String, Object> params)
      throws StreamException {
    checkNotNull(resource, "Resource can't be empty");
    checkArgument(!resource.isEmpty(), "Resource can't be empty");
    checkNotNull(params, "Missing params");

    try {
      final URL url = buildPersonalizationURL(baseURL, resource + '/');
      final RequestOption[] options =
          StreamSupport.stream(params.entrySet())
              .map(entry -> new CustomQueryParameter(entry.getKey(), entry.getValue().toString()))
              .toArray(RequestOption[]::new);
      return httpClient
          .execute(buildGet(url, key, token, options))
          .thenApply(
              response -> {
                try {
                  return deserialize(response, new TypeReference<Map<String, Object>>() {});
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Void> post(
      Token token,
      String userID,
      String resource,
      Map<String, Object> params,
      Map<String, Object> payload)
      throws StreamException {
    checkNotNull(resource, "Resource can't be empty");
    checkArgument(!resource.isEmpty(), "Resource can't be empty");
    checkNotNull(params, "Missing params");
    checkNotNull(params, "Missing payload");

    try {
      final byte[] jsonPayload =
          toJSON(
              new Object() {
                public final Map<String, Object> data = payload;
              });
      final URL url = buildPersonalizationURL(baseURL, resource + '/');
      final RequestOption[] options =
          StreamSupport.stream(params.entrySet())
              .map(entry -> new CustomQueryParameter(entry.getKey(), entry.getValue().toString()))
              .toArray(RequestOption[]::new);
      return httpClient
          .execute(buildPost(url, key, token, jsonPayload, options))
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

  public CompletableFuture<Void> delete(
      Token token, String userID, String resource, Map<String, Object> params)
      throws StreamException {
    checkNotNull(resource, "Resource can't be empty");
    checkArgument(!resource.isEmpty(), "Resource can't be empty");
    checkNotNull(params, "Missing params");

    try {
      final URL url = buildPersonalizationURL(baseURL, resource + '/');
      final RequestOption[] options =
          params.entrySet().stream()
              .map(entry -> new CustomQueryParameter(entry.getKey(), entry.getValue().toString()))
              .toArray(RequestOption[]::new);
      return httpClient
          .execute(buildDelete(url, key, token, options))
          .thenApply(
              response -> {
                try {
                  return deserializeError(response);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }
}
