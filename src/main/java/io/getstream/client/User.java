package io.getstream.client;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Data;
import io.getstream.core.models.ProfileData;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.deserialize;
import static io.getstream.core.utils.Serialization.deserializeError;

public class User {
    private final Client client;
    private final String id;

    public User(Client client, String id) {
        checkNotNull(client, "Client can't be null");
        checkNotNull(id, "User ID can't be null");
        checkArgument(!id.isEmpty(), "User ID can't be empty");

        this.client = client;
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public CompletableFuture<Data> get() throws StreamException {
        return client.getUser(id)
                .thenApply(response -> {
                    try {
                        return deserialize(response, Data.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<Void> delete() throws StreamException {
        return client.deleteUser(id)
                .thenApply(response -> {
                    try {
                        return deserializeError(response);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<Data> getOrCreate(Data data) throws StreamException {
        return client.getOrCreateUser(id, data)
                .thenApply(response -> {
                    try {
                        return deserialize(response, Data.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<Data> create(Data data) throws StreamException {
        return client.createUser(id, data)
                .thenApply(response -> {
                    try {
                        return deserialize(response, Data.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<Data> update(Data data) throws StreamException {
        return client.updateUser(id, data)
                .thenApply(response -> {
                    try {
                        return deserialize(response, Data.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<ProfileData> profile() throws StreamException {
        return client.userProfile(id)
                .thenApply(response -> {
                    try {
                        return deserialize(response, ProfileData.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }
}
