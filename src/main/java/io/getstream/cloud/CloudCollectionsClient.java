package io.getstream.cloud;

import io.getstream.core.StreamCollections;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.CollectionData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static io.getstream.core.utils.Serialization.convert;

public final class CloudCollectionsClient {
    private final Token token;
    private final String userID;
    private final StreamCollections collections;

    CloudCollectionsClient(Token token, String userID, StreamCollections collections) {
        this.token = token;
        this.userID = userID;
        this.collections = collections;
    }

    public <T> CompletableFuture<T> addCustom(String collection, T item) throws StreamException {
        return addCustom(userID, collection, item);
    }

    public <T> CompletableFuture<T> addCustom(String userID, String collection, T item) throws StreamException {
        return add(userID, collection, convert(item, CollectionData.class))
                .thenApply(data -> convert(data, (Class<T>) item.getClass()));
    }

    public CompletableFuture<CollectionData> add(String collection, CollectionData item) throws StreamException {
        return add(userID, collection, item);
    }

    public CompletableFuture<CollectionData> add(String userID, String collection, CollectionData item) throws StreamException {
        return collections.add(token, userID, collection, item);
    }

    public <T> CompletableFuture<T> updateCustom(String collection, T item) throws StreamException {
        return updateCustom(userID, collection, item);
    }

    public <T> CompletableFuture<T> updateCustom(String userID, String collection, T item) throws StreamException {
        return update(userID, collection, convert(item, CollectionData.class))
                .thenApply(data -> convert(data, (Class<T>) item.getClass()));
    }

    public CompletableFuture<CollectionData> update(String collection, CollectionData item) throws StreamException {
        return update(userID, collection, item);
    }

    public CompletableFuture<CollectionData> update(String userID, String collection, CollectionData item) throws StreamException {
        return collections.update(token, userID, collection, item);
    }

    public <T> CompletableFuture<T> getCustom(Class<T> type, String collection, String id) throws StreamException {
        return get(collection, id).thenApply(data -> convert(data, type));
    }

    public CompletableFuture<CollectionData> get(String collection, String id) throws StreamException {
        return collections.get(token, collection, id);
    }

    public CompletableFuture<Void> delete(String collection, String id) throws StreamException {
        return collections.delete(token, collection, id);
    }
}
