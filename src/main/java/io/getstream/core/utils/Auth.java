package io.getstream.core.utils;

import static com.google.common.base.MoreObjects.firstNonNull;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import io.getstream.core.http.Token;
import io.getstream.core.models.FeedID;
import java.util.Date;

public final class Auth {
  public enum TokenAction {
    ANY("*"),
    READ("read"),
    WRITE("write"),
    DELETE("delete");

    private final String action;

    TokenAction(String action) {
      this.action = action;
    }

    @Override
    public String toString() {
      return action;
    }
  }

  public enum TokenResource {
    ANY("*"),
    ACTIVITIES("activities"),
    ANALYTICS("analytics"),
    ANALYTICS_REDIRECT("redirect_and_track"),
    COLLECTIONS("collections"),
    FILES("files"),
    FEED("feed"),
    FEED_TARGETS("feed_targets"),
    FOLLOWER("follower"),
    OPEN_GRAPH("url"),
    PERSONALIZATION("personalization"),
    REACTIONS("reactions"),
    USERS("users"),
    MODERATION("moderation"),
    DATAPRIVACY("data_privacy"),
    AUDITLOGS("audit_logs");

    private final String resource;

    TokenResource(String resource) {
      this.resource = resource;
    }

    @Override
    public String toString() {
      return resource;
    }
  }

  private Auth() {
    /* nothing to see here */
  }

  public static Token buildFeedToken(String secret, FeedID feed, TokenAction action) {
    return buildBackendToken(secret, TokenResource.FEED, action, feed.getSlug() + feed.getUserID());
  }

  public static Token buildToTargetUpdateToken(String secret, FeedID feed, TokenAction action) {
    return buildBackendToken(
        secret, TokenResource.FEED_TARGETS, action, feed.getSlug() + feed.getUserID());
  }

  public static Token buildFeedToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.FEED, action, "*");
  }

  public static Token buildActivityToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.ACTIVITIES, action, "*");
  }

  public static Token buildFollowToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.FOLLOWER, action, "*");
  }

  public static Token buildFollowToken(String secret, FeedID feed, TokenAction action) {
    return buildBackendToken(
        secret, TokenResource.FOLLOWER, action, feed.getSlug() + feed.getUserID());
  }

  public static Token buildCollectionsToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.COLLECTIONS, action, "*");
  }

  public static Token buildPersonalizationToken(String secret, String userID, TokenAction action) {
    return buildBackendToken(
        secret, TokenResource.PERSONALIZATION, action, "*", firstNonNull(userID, "*"));
  }

  public static Token buildReactionsToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.REACTIONS, action, "*");
  }

  public static Token buildModerationToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.MODERATION, action, "*");
  }

  public static Token buildDataPrivacyToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.DATAPRIVACY, action, "*");
  }

  public static Token buildAnalyticsToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.ANALYTICS, action, "*");
  }

  public static Token buildAnalyticsRedirectToken(String secret) {
    return buildBackendToken(secret, TokenResource.ANALYTICS_REDIRECT, TokenAction.ANY, "*");
  }

  public static Token buildUsersToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.USERS, action, "*");
  }

  public static Token buildOpenGraphToken(String secret) {
    return buildBackendToken(secret, TokenResource.OPEN_GRAPH, TokenAction.READ, "*");
  }

  public static Token buildFilesToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.FILES, action, "*");
  }

  public static Token buildAuditLogsToken(String secret, TokenAction action) {
    return buildBackendToken(secret, TokenResource.AUDITLOGS, action, "*");
  }

  public static Token buildFrontendToken(String secret, String userID) {
    return buildFrontendToken(secret, userID, null);
  }

  public static Token buildFrontendToken(String secret, String userID, Date expiresAt) {
    final Algorithm algorithm = Algorithm.HMAC256(secret);
    JWTCreator.Builder builder = JWT.create().withClaim("user_id", userID);
    if (expiresAt != null) {
      builder = builder.withExpiresAt(expiresAt);
    }
    return new Token(builder.sign(algorithm));
  }

  public static Token buildBackendToken(
      String secret, TokenResource resource, TokenAction action, String feedID) {
    return buildBackendToken(secret, resource, action, feedID, null);
  }

  public static Token buildBackendToken(
      String secret, TokenResource resource, TokenAction action, String feedID, String userID) {
    final Algorithm algorithm = Algorithm.HMAC256(secret);
    JWTCreator.Builder builder = JWT.create();
    builder.withClaim("resource", resource.toString());
    builder.withClaim("action", action.toString());
    builder.withClaim("feed_id", feedID);
    if (userID != null) {
      builder.withClaim("user_id", userID);
    }
    return new Token(builder.sign(algorithm));
  }
}
