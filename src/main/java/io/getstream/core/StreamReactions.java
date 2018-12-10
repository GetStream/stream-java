package io.getstream.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.Reaction;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.options.Filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Request.*;
import static io.getstream.core.utils.Routes.buildReactionsURL;
import static io.getstream.core.utils.Serialization.*;

public final class StreamReactions {
    private final String key;
    private final URL baseURL;
    private final HTTPClient httpClient;

    StreamReactions(String key, URL baseURL, HTTPClient httpClient) {
        this.key = key;
        this.baseURL = baseURL;
        this.httpClient = httpClient;
    }

    public CompletableFuture<Reaction> get(Token token, String id) throws StreamException {
        checkNotNull(id, "Reaction id can't be null");
        checkArgument(!id.isEmpty(), "Reaction id can't be empty");

        try {
            final URL url = buildReactionsURL(baseURL, id + '/');
            return httpClient.execute(buildGet(url, key, token))
                    .thenApply(response -> {
                        try {
                            return deserialize(response, Reaction.class);
                        } catch (StreamException | IOException e) {
                            throw new CompletionException(e);
                        }
                    });
        } catch (MalformedURLException | URISyntaxException e) {
            throw new StreamException(e);
        }
    }

    public CompletableFuture<List<Reaction>> filter(Token token, LookupKind lookup, String id, Filter filter, String kind) throws StreamException {
        checkNotNull(lookup, "Lookup kind can't be null");
        checkNotNull(id, "Reaction id can't be null");
        checkArgument(!id.isEmpty(), "Reaction id can't be empty");
        checkNotNull(filter, "Filter can't be null");
        checkNotNull(kind, "Kind can't be null");

        try {
            final URL url = buildReactionsURL(baseURL, lookup.getKind() + '/' + id + '/');
            return httpClient.execute(buildGet(url, key, token, filter, new CustomQueryParameter("kind", kind)))
                    .thenApply(response -> {
                        try {
                            return deserializeContainer(response, Reaction.class);
                        } catch (StreamException | IOException e) {
                            throw new CompletionException(e);
                        }
                    });
        } catch (MalformedURLException | URISyntaxException e) {
            throw new StreamException(e);
        }
    }

    public CompletableFuture<Reaction> add(Token token, String userID, Reaction reaction, FeedID... targetFeeds) throws StreamException {
        checkNotNull(reaction, "Reaction can't be null");
        checkNotNull(reaction.getActivityID(), "Reaction activity id can't be null");
        checkArgument(!reaction.getActivityID().isEmpty(), "Reaction activity id can't be empty");
        checkNotNull(reaction.getKind(), "Reaction kind can't be null");
        checkArgument(!reaction.getKind().isEmpty(), "Reaction kind can't be empty");

        String[] targetFeedIDs = Arrays.stream(targetFeeds)
                .map(feed -> feed.toString())
                .toArray(String[]::new);

        try {
            ImmutableMap.Builder<String, Object> payloadBuilder = ImmutableMap.builder();
            payloadBuilder.put("activity_id", reaction.getActivityID());
            payloadBuilder.put("kind", reaction.getKind());
            payloadBuilder.put("target_feeds", targetFeedIDs);
            if (userID != null || reaction.getUserID() != null) {
                payloadBuilder.put("user_id", firstNonNull(userID, reaction.getUserID()));
            }
            if (reaction.getId() != null) {
                payloadBuilder.put("id", reaction.getId());
            }
            if (reaction.getExtra() != null) {
                payloadBuilder.put("data", reaction.getExtra());
            }
            final byte[] payload = toJSON(payloadBuilder.build());
            final URL url = buildReactionsURL(baseURL);
            return httpClient.execute(buildPost(url, key, token, payload))
                    .thenApply(response -> {
                        try {
                            return deserialize(response, Reaction.class);
                        } catch (StreamException | IOException e) {
                            throw new CompletionException(e);
                        }
                    });
        } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
            throw new StreamException(e);
        }
    }

    public CompletableFuture<Void> update(Token token, Reaction reaction, FeedID... targetFeeds) throws StreamException {
        checkNotNull(reaction, "Reaction can't be null");
        checkNotNull(reaction.getId(), "Reaction id can't be null");
        checkArgument(!reaction.getId().isEmpty(), "Reaction id can't be empty");

        String[] targetFeedIDs = Arrays.stream(targetFeeds)
                .map(feed -> feed.toString())
                .toArray(String[]::new);

        try {
            final byte[] payload = toJSON(new Object() {
                public final Map<String, Object> data = reaction.getExtra();
                public final String[] target_feeds = targetFeedIDs;
            });
            final URL url = buildReactionsURL(baseURL, reaction.getId() + '/');
            return httpClient.execute(buildPut(url, key, token, payload))
                    .thenApply(response -> {
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

    public CompletableFuture<Void> delete(Token token, String id) throws StreamException {
        checkNotNull(id, "Reaction id can't be null");
        checkArgument(!id.isEmpty(), "Reaction id can't be empty");

        try {
            final URL url = buildReactionsURL(baseURL, id + '/');
            return httpClient.execute(buildDelete(url, key, token))
                    .thenApply(response -> {
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
