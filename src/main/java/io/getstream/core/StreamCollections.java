package io.getstream.core;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Request.*;
import static io.getstream.core.utils.Routes.buildBatchCollectionsURL;
import static io.getstream.core.utils.Routes.buildCollectionsURL;
import static io.getstream.core.utils.Serialization.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.models.CollectionData;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.options.RequestOption;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java8.util.J8Arrays;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;
import java8.util.stream.Collectors;

public final class StreamCollections {
  private final String key;
  private final URL baseURL;
  private final HTTPClient httpClient;

  StreamCollections(String key, URL baseURL, HTTPClient httpClient) {
    this.key = key;
    this.baseURL = baseURL;
    this.httpClient = httpClient;
  }

  public CompletableFuture<CollectionData> add(
      Token token, String userID, String collection, CollectionData item) throws StreamException {
    checkNotNull(collection, "Collection name can't be null");
    checkArgument(!collection.isEmpty(), "Collection name can't be empty");
    checkNotNull(item, "Collection data can't be null");

    try {
      ImmutableMap.Builder builder =
          new ImmutableMap.Builder<String, Object>().put("data", item.getData());
      if (userID != null) {
        builder.put("user_id", userID);
      }
      if (item.getID() != null) {
        builder.put("id", item.getID());
      }
      final byte[] payload = toJSON(builder.build());
      final URL url = buildCollectionsURL(baseURL, collection + '/');
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserialize(response, CollectionData.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<CollectionData> update(
      Token token, String userID, String collection, CollectionData item) throws StreamException {
    checkNotNull(collection, "Collection name can't be null");
    checkArgument(!collection.isEmpty(), "Collection name can't be empty");
    checkNotNull(item, "Collection data can't be null");

    try {
      ImmutableMap.Builder builder =
          new ImmutableMap.Builder<String, Object>().put("data", item.getData());
      if (userID != null) {
        builder.put("user_id", userID);
      }
      final byte[] payload = toJSON(builder.build());
      final URL url = buildCollectionsURL(baseURL, collection + '/' + item.getID() + '/');
      return httpClient
          .execute(buildPut(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserialize(response, CollectionData.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Void> upsert(Token token, String collection, CollectionData... items)
      throws StreamException {
    checkNotNull(collection, "Collection name can't be null");
    checkArgument(!collection.isEmpty(), "Collection name can't be empty");
    checkArgument(items.length > 0, "Collection data can't be empty");

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public final Map<String, CollectionData[]> data =
                    ImmutableMap.of(collection, items);
              });
      final URL url = buildBatchCollectionsURL(baseURL);
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

  public CompletableFuture<CollectionData> get(Token token, String collection, String id)
      throws StreamException {
    checkNotNull(collection, "Collection name can't be null");
    checkArgument(!collection.isEmpty(), "Collection name can't be empty");
    checkNotNull(id, "Collection id can't be null");
    checkArgument(!id.isEmpty(), "Collection id can't be empty");

    try {
      final URL url = buildCollectionsURL(baseURL, collection + '/' + id + '/');
      return httpClient
          .execute(buildGet(url, key, token))
          .thenApply(
              response -> {
                try {
                  return deserialize(response, CollectionData.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<List<CollectionData>> select(
      Token token, String collection, String... ids) throws StreamException {
    checkNotNull(collection, "Collection name can't be null");
    checkArgument(!collection.isEmpty(), "Collection name can't be empty");
    checkArgument(ids.length > 0, "Collection ids can't be empty");

    List<String> foreignIDs =
        J8Arrays.stream(ids)
            .map(id -> String.format("%s:%s", collection, id))
            .collect(Collectors.toList());
    try {
      final URL url = buildBatchCollectionsURL(baseURL);
      return httpClient
          .execute(
              buildGet(
                  url,
                  key,
                  token,
                  new CustomQueryParameter("foreign_ids", Joiner.on(",").join(foreignIDs))))
          .thenApply(
              response -> {
                try {
                  return deserializeContainer(response, "response.data", CollectionData.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Void> delete(Token token, String collection, String id)
      throws StreamException {
    checkNotNull(collection, "Collection name can't be null");
    checkArgument(!collection.isEmpty(), "Collection name can't be empty");
    checkNotNull(id, "Collection id can't be null");
    checkArgument(!id.isEmpty(), "Collection id can't be empty");

    try {
      final URL url = buildCollectionsURL(baseURL, collection + '/' + id + '/');
      return httpClient
          .execute(buildDelete(url, key, token))
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

  public CompletableFuture<Void> deleteMany(Token token, String collection, String... ids)
      throws StreamException {
    checkNotNull(collection, "Collection name can't be null");
    checkArgument(!collection.isEmpty(), "Collection name can't be empty");
    checkArgument(ids.length > 0, "Collection ids can't be empty");

    try {
      final URL url = buildBatchCollectionsURL(baseURL);
      final RequestOption[] options =
          new RequestOption[] {
            new CustomQueryParameter("collection_name", collection),
            new CustomQueryParameter("ids", Joiner.on(",").join(ids))
          };
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
