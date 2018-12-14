package io.getstream.client;

import com.google.common.collect.ImmutableMap;
import io.getstream.core.StreamPersonalization;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.utils.Auth.TokenAction;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static io.getstream.core.utils.Auth.buildPersonalizationToken;

public final class PersonalizationClient {
    private final String secret;
    private final StreamPersonalization personalization;

    PersonalizationClient(String secret, StreamPersonalization personalization) {
        this.secret = secret;
        this.personalization = personalization;
    }

    public CompletableFuture<Map<String, Object>> get(String resource) throws StreamException {
        return get(null, resource);
    }

    public CompletableFuture<Map<String, Object>> get(String resource, Map<String, Object> params) throws StreamException {
        return get(null, resource, params);
    }

    public CompletableFuture<Map<String, Object>> get(String userID, String resource) throws StreamException {
        return get(userID, resource, ImmutableMap.of());
    }

    public CompletableFuture<Map<String, Object>> get(String userID, String resource, Map<String, Object> params) throws StreamException {
        final Token token = buildPersonalizationToken(secret, userID, TokenAction.READ);
        return personalization.get(token, userID, resource, params);
    }

    public CompletableFuture<Void> post(String resource, Map<String, Object> payload) throws StreamException {
        return post(null, resource, payload);
    }

    public CompletableFuture<Void> post(String resource, Map<String, Object> params, Map<String, Object> payload) throws StreamException {
        return post(null, resource, params, payload);
    }

    public CompletableFuture<Void> post(String userID, String resource, Map<String, Object> payload) throws StreamException {
        return post(userID, resource, ImmutableMap.of(), payload);
    }

    public CompletableFuture<Void> post(String userID, String resource, Map<String, Object> params, Map<String, Object> payload) throws StreamException {
        final Token token = buildPersonalizationToken(secret, userID, TokenAction.WRITE);
        return personalization.post(token, userID, resource, params, payload);
    }

    public CompletableFuture<Void> delete(String resource) throws StreamException {
        return delete(null, resource);
    }

    public CompletableFuture<Void> delete(String resource, Map<String, Object> params) throws StreamException {
        return delete(null, resource, params);
    }

    public CompletableFuture<Void> delete(String userID, String resource) throws StreamException {
        return delete(userID, resource, ImmutableMap.of());
    }

    public CompletableFuture<Void> delete(String userID, String resource, Map<String, Object> params) throws StreamException {
        final Token token = buildPersonalizationToken(secret, userID, TokenAction.DELETE);
        return personalization.delete(token, userID, resource, params);
    }
}
