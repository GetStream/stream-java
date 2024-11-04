package io.getstream.client;

import static io.getstream.core.utils.Auth.buildModerationToken;
import static io.getstream.core.utils.Auth.buildReactionsToken;
import static io.getstream.core.utils.Routes.*;
import static io.getstream.core.utils.Serialization.*;

import io.getstream.core.Moderation;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Response;
import io.getstream.core.http.Token;
import io.getstream.core.utils.Auth;
import java.util.Map;
import java8.util.concurrent.CompletableFuture;

public class ModerationClient {
  private final String secret;
  private final Moderation mod;

  ModerationClient(String secret, Moderation mod) {
    this.secret = secret;
    this.mod = mod;
  }

  public CompletableFuture<Response> flagUser(
      String flaggedUserId, String reason, Map<String, Object> options) throws StreamException {
    return flag("stream:user", flaggedUserId, "", reason, options);
  }

  public CompletableFuture<Response> flagActivity(
      String entityId, String entityCreatorId, String reason, Map<String, Object> options)
      throws StreamException {
    return flag("stream:feeds:v2:activity", entityId, entityCreatorId, reason, options);
  }

  public CompletableFuture<Response> flagReaction(
      String entityId, String reportingUser, String reason, Map<String, Object> options)
      throws StreamException {
    return flag("stream:feeds:v2:reaction", entityId, reportingUser, reason, options);
  }

  private CompletableFuture<Response> flag(
      String entityType,
      String entityId,
      String reportingUser,
      String reason,
      Map<String, Object> options)
      throws StreamException {
    final Token token = buildModerationToken(secret, Auth.TokenAction.WRITE);
    return mod.flag(token, entityType, entityId, reportingUser, reason, options);
  }
}
