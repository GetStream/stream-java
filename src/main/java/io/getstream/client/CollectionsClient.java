package io.getstream.client;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import io.getstream.core.StreamCollections;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.CollectionData;
import io.getstream.core.utils.Auth.TokenAction;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static io.getstream.core.utils.Auth.buildCollectionsToken;
import static io.getstream.core.utils.Serialization.convert;

public final class CollectionsClient {
    private final String secret;
    private final StreamCollections collections;

    CollectionsClient(String secret, StreamCollections collections) {
        this.secret = secret;
        this.collections = collections;
    }

    public <T> CompletableFuture<T> addCustom(String collection, T item) throws StreamException {
        return addCustom(null, collection, item);
    }

    public <T> CompletableFuture<T> addCustom(String userID, String collection, T item) throws StreamException {
        return add(userID, collection, convert(item, CollectionData.class))
                .thenApply(data -> convert(data, (Class<T>) item.getClass()));
    }

    public CompletableFuture<CollectionData> add(String collection, CollectionData item) throws StreamException {
        return add(null, collection, item);
    }

    public CompletableFuture<CollectionData> add(String userID, String collection, CollectionData item) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.WRITE);
        return collections.add(token, userID, collection, item);
    }

    public <T> CompletableFuture<T> updateCustom(String collection, T item) throws StreamException {
        return updateCustom(null, collection, item);
    }

    public <T> CompletableFuture<T> updateCustom(String userID, String collection, T item) throws StreamException {
        return update(userID, collection, convert(item, CollectionData.class))
                .thenApply(data -> convert(data, (Class<T>) item.getClass()));
    }

    public CompletableFuture<CollectionData> update(String collection, CollectionData item) throws StreamException {
        return update(null, collection, item);
    }

    public CompletableFuture<CollectionData> update(String userID, String collection, CollectionData item) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.WRITE);
        return collections.update(token, userID, collection, item);
    }

    public <T> CompletableFuture<Void> upsertManyCustom(String collection, Iterable<T> items) throws StreamException {
        final CollectionData[] custom = Streams.stream(items)
                .map(item -> CollectionData.buildFrom(item))
                .toArray(CollectionData[]::new);
        return upsertMany(collection, custom);
    }

    public <T> CompletableFuture<Void> upsertManyCustom(String collection, T... items) throws StreamException {
        final CollectionData[] custom = Arrays.stream(items)
                .map(item -> CollectionData.buildFrom(item))
                .toArray(CollectionData[]::new);
        return upsertMany(collection, custom);
    }

    public CompletableFuture<Void> upsertMany(String collection, Iterable<CollectionData> items) throws StreamException {
        return upsertMany(collection, Iterables.toArray(items, CollectionData.class));
    }

    public CompletableFuture<Void> upsertMany(String collection, CollectionData... items) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.WRITE);
        return collections.upsertMany(token, collection, items);
    }

    public <T> CompletableFuture<List<T>> customItems(Class<T> type, String collection) throws StreamException {
        return items(collection)
                .thenApply(result -> result.stream()
                        .map(item -> convert(item, type))
                        .collect(Collectors.toList()));
    }

    public CompletableFuture<List<CollectionData>> items(String collection) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.READ);
        return collections.items(token, collection);
    }

    public <T> CompletableFuture<T> getCustom(Class<T> type, String collection, String id) throws StreamException {
        return get(collection, id).thenApply(data -> convert(data, type));
    }

    public CompletableFuture<CollectionData> get(String collection, String id) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.READ);
        return collections.get(token, collection, id);
    }

    public <T> CompletableFuture<List<T>> getManyCustom(Class<T> type, String collection, Iterable<String> ids) throws StreamException {
        return getManyCustom(type, collection, Iterables.toArray(ids, String.class));
    }

    public <T> CompletableFuture<List<T>> getManyCustom(Class<T> type, String collection, String... ids) throws StreamException {
        return getMany(collection, ids)
                .thenApply(data -> data.stream().map(item -> convert(item, type)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<CollectionData>> getMany(String collection, Iterable<String> ids) throws StreamException {
        return getMany(collection, Iterables.toArray(ids, String.class));
    }

    public CompletableFuture<List<CollectionData>> getMany(String collection, String... ids) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.READ);
        return collections.getMany(token, collection, ids);
    }

    public CompletableFuture<Void> delete(String collection, String id) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.DELETE);
        return collections.delete(token, collection, id);
    }

    public CompletableFuture<Void> deleteMany(String collection, Iterable<String> ids) throws StreamException {
        return deleteMany(collection, Iterables.toArray(ids, String.class));
    }

    public CompletableFuture<Void> deleteMany(String collection, String... ids) throws StreamException {
        final Token token = buildCollectionsToken(secret, TokenAction.DELETE);
        return collections.deleteMany(token, collection, ids);
    }
}
