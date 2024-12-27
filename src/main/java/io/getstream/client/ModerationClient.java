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
      String flaggedUserId,String reportingUser, String reason, Map<String, Object> custom) throws StreamException {
    return flag("stream:user", flaggedUserId, reportingUser, reason, custom);
  }

  public CompletableFuture<Response> flagActivity(
      String entityId, String reportingUser, String reason, Map<String, Object> custom)
      throws StreamException {
    return flag("stream:feeds:v2:activity", entityId, reportingUser, reason, custom);
  }

  public CompletableFuture<Response> flagReaction(
      String entityId, String reportingUser, String reason, Map<String, Object> custom)
      throws StreamException {
    return flag("stream:feeds:v2:reaction", entityId, reportingUser, reason, custom);
  }

  private CompletableFuture<Response> flag(
      String entityType,
      String entityId,
      String reportingUser,
      String reason,
      Map<String, Object> custom)
      throws StreamException {
    final Token token = buildModerationToken(secret, Auth.TokenAction.WRITE);
    return mod.flag(token, entityType, entityId, reportingUser, reason, custom);
  }
}
