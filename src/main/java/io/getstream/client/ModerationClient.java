package io.getstream.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.getstream.core.Moderation;
import io.getstream.core.StreamBatch;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.utils.Auth;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;
import io.getstream.core.http.Response;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import static io.getstream.core.utils.Auth.buildReactionsToken;
import static io.getstream.core.utils.Routes.*;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Request.buildPost;
import static io.getstream.core.utils.Serialization.*;

public class ModerationClient {
    private final String secret;
    private final Moderation mod;

    ModerationClient(String secret, Moderation mod) {
        this.secret = secret;
        this.mod = mod;
    }

    public CompletableFuture<Response> flagUser(String flaggedUserId, String reason, Map<String, Object> options)throws StreamException {
        return flag("stream:user", flaggedUserId, "", reason, options);
    }

    public CompletableFuture<Response> flagActivity(String entityId, String entityCreatorId, String reason, Map<String, Object> options) throws StreamException{
        return flag("stream:feeds:v2:activity", entityId, entityCreatorId, reason, options);
    }

    public CompletableFuture<Response> flagReaction(String entityId, String entityCreatorId, String reason, Map<String, Object> options) throws StreamException{
        return flag("stream:feeds:v2:reaction", entityId, entityCreatorId, reason, options);
    }

    private CompletableFuture<Response> flag(String entityType, String entityId, String entityCreatorId,
                                                 String reason, Map<String, Object> options)throws StreamException {
        final Token token = buildReactionsToken(secret, Auth.TokenAction.WRITE);
        return mod.flag(token, entityType, entityId, entityCreatorId, reason, options);
    }
}
