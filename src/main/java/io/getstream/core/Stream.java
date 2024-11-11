package io.getstream.core;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Request.*;
import static io.getstream.core.utils.Routes.*;
import static io.getstream.core.utils.Serialization.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Response;
import io.getstream.core.http.Token;
import io.getstream.core.models.*;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.options.RequestOption;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java8.util.J8Arrays;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public final class Stream {
  private final String key;
  private final URL baseURL;
  private final HTTPClient httpClient;

  public Stream(String key, URL baseURL, HTTPClient httpClient) {
    this.key = key;
    this.baseURL = baseURL;
    this.httpClient = httpClient;
  }

  public StreamBatch batch() {
    return new StreamBatch(key, baseURL, httpClient);
  }

  public StreamCollections collections() {
    return new StreamCollections(key, baseURL, httpClient);
  }

  public StreamPersonalization personalization() {
    return new StreamPersonalization(key, baseURL, httpClient);
  }

  public StreamAnalytics analytics() {
    return new StreamAnalytics(key, baseURL, httpClient);
  }

  public StreamReactions reactions() {
    return new StreamReactions(key, baseURL, httpClient);
  }

  public Moderation moderation() {
    return new Moderation(key, baseURL, httpClient);
  }

  public StreamFiles files() {
    return new StreamFiles(key, baseURL, httpClient);
  }

  public StreamImages images() {
    return new StreamImages(key, baseURL, httpClient);
  }

  public CompletableFuture<List<Activity>> updateActivitiesByID(
      Token token, ActivityUpdate[] updates) throws StreamException {
    checkNotNull(updates, "No updates");
    checkArgument(updates.length > 0, "No updates");
    for (ActivityUpdate update : updates) {
      checkNotNull(update.getID(), "No activity to update");
      checkNotNull(update.getSet(), "No activity properties to set");
      checkNotNull(update.getUnset(), "No activity properties to unset");
    }

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public final ActivityUpdate[] changes = updates;
              });
      final URL url = buildActivityUpdateURL(baseURL);
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserializeContainer(response, "activities", Activity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Activity> updateActivityByID(
      Token token, String id, Map<String, Object> set, String[] unset) throws StreamException {
    checkNotNull(id, "No activity to update");
    checkNotNull(set, "No activity properties to set");
    checkNotNull(unset, "No activity properties to unset");

    try {
      // XXX: renaming variables so we can unambiguously name payload fields 'id', 'set', 'unset'
      String activityID = id;
      Map<String, Object> propertiesToSet = set;
      String[] propertiesToUnset = unset;
      final byte[] payload =
          toJSON(
              new Object() {
                public final String id = activityID;
                public final Map<String, Object> set = propertiesToSet;
                public final String[] unset = propertiesToUnset;
              });
      final URL url = buildActivityUpdateURL(baseURL);
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserialize(response, Activity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<List<Activity>> updateActivitiesByForeignID(
      Token token, ActivityUpdate[] updates) throws StreamException {
    checkNotNull(updates, "No updates");
    checkArgument(updates.length > 0, "No updates");
    for (ActivityUpdate update : updates) {
      checkNotNull(update.getForeignID(), "No activity to update");
      checkNotNull(update.getTime(), "Missing timestamp");
      checkNotNull(update.getSet(), "No activity properties to set");
      checkNotNull(update.getUnset(), "No activity properties to unset");
    }

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public final ActivityUpdate[] changes = updates;
              });
      final URL url = buildActivityUpdateURL(baseURL);
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserializeContainer(response, "activities", Activity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Activity> updateActivityByForeignID(
      Token token, String foreignID, Date timestamp, Map<String, Object> set, String[] unset)
      throws StreamException {
    checkNotNull(foreignID, "No activity to update");
    checkNotNull(timestamp, "Missing timestamp");
    checkNotNull(set, "No activity properties to set");
    checkNotNull(unset, "No activity properties to unset");

    try {
      // XXX: renaming variables so we can unambiguously name payload fields 'set', 'unset'
      Map<String, Object> propertiesToSet = set;
      String[] propertiesToUnset = unset;
      final byte[] payload =
          toJSON(
              new Object() {
                public final String foreign_id = foreignID;

                @JsonFormat(
                    shape = JsonFormat.Shape.STRING,
                    pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    lenient = OptBoolean.FALSE,
                    timezone = "UTC")
                public final Date time = timestamp;

                public final Map<String, Object> set = propertiesToSet;
                public final String[] unset = propertiesToUnset;
              });
      final URL url = buildActivityUpdateURL(baseURL);
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
                try {
                  return deserialize(response, Activity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<OGData> openGraph(Token token, URL targetURL) throws StreamException {
    checkNotNull(targetURL, "Missing url");

    try {
      final URL url = buildOpenGraphURL(baseURL);
      return httpClient
          .execute(
              buildGet(
                  url, key, token, new CustomQueryParameter("url", targetURL.toExternalForm())))
          .thenApply(
              response -> {
                try {
                  return deserialize(response, OGData.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public <T> T getHTTPClientImplementation() {
    return httpClient.getImplementation();
  }

  public CompletableFuture<Response> getActivities(
      Token token, FeedID feed, RequestOption... options) throws StreamException {
    checkNotNull(options, "Missing request options");

    try {
      final URL url = buildFeedURL(baseURL, feed, "/");
      return httpClient.execute(buildGet(url, key, token, options));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> getEnrichedActivities(
      Token token, FeedID feed, RequestOption... options) throws StreamException {
    checkNotNull(options, "Missing request options");

    try {
      final URL url = buildEnrichedFeedURL(baseURL, feed, "/");
      return httpClient.execute(buildGet(url, key, token, options));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> addActivity(Token token, FeedID feed, Activity activity)
      throws StreamException {
    checkNotNull(activity, "No activity to add");

    try {
      final byte[] payload = toJSON(activity);
      final URL url = buildFeedURL(baseURL, feed, "/");
      return httpClient.execute(buildPost(url, key, token, payload));
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> addActivities(
      Token token, FeedID feed, Activity... activityObjects) throws StreamException {
    checkNotNull(activityObjects, "No activities to add");

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public final Activity[] activities = activityObjects;
              });
      final URL url = buildFeedURL(baseURL, feed, "/");
      return httpClient.execute(buildPost(url, key, token, payload));
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> removeActivityByID(Token token, FeedID feed, String id)
      throws StreamException {
    checkNotNull(id, "No activity id to remove");

    try {
      final URL url = buildFeedURL(baseURL, feed, '/' + id + '/');
      return httpClient.execute(buildDelete(url, key, token));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> removeActivityByForeignID(
      Token token, FeedID feed, String foreignID) throws StreamException {
    checkNotNull(foreignID, "No activity id to remove");

    try {
      final URL url = buildFeedURL(baseURL, feed, '/' + foreignID + '/');
      return httpClient.execute(
          buildDelete(url, key, token, new CustomQueryParameter("foreign_id", "1")));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> follow(
      Token token, Token targetToken, FeedID sourceFeed, FeedID targetFeed, int activityCopyLimit)
      throws StreamException {
    checkNotNull(targetFeed, "No feed to follow");
    checkArgument(sourceFeed != targetFeed, "Feed can't follow itself");
    checkArgument(activityCopyLimit >= 0, "Activity copy limit should be a non-negative number");

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public String target = targetFeed.toString();
                public int activity_copy_limit = activityCopyLimit;
                public String target_token = targetToken.toString();
              });
      final URL url = buildFeedURL(baseURL, sourceFeed, "/following/");
      return httpClient.execute(buildPost(url, key, token, payload));
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> getFollowers(
      Token token, FeedID feed, RequestOption... options) throws StreamException {
    checkNotNull(options, "Missing request options");

    try {
      final URL url = buildFeedURL(baseURL, feed, "/followers/");
      return httpClient.execute(buildGet(url, key, token, options));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> getFollowed(Token token, FeedID feed, RequestOption... options)
      throws StreamException {
    checkNotNull(options, "Missing request options");

    try {
      final URL url = buildFeedURL(baseURL, feed, "/following/");
      return httpClient.execute(buildGet(url, key, token, options));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> unfollow(
      Token token, FeedID source, FeedID target, RequestOption... options) throws StreamException {
    checkNotNull(options, "Missing request options");
    checkNotNull(target, "No target feed to unfollow");

    try {
      final URL url = buildFeedURL(baseURL, source, "/following/" + target + '/');
      return httpClient.execute(buildDelete(url, key, token, options));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> getFollowStats(
      Token token,
      FeedID feed,
      String[] followerSlugs,
      String[] followingSlugs,
      RequestOption... options)
      throws StreamException {
    try {
      final URL url = followStatsPath(baseURL);
      final List<CustomQueryParameter> params = new ArrayList<>(4);
      final String feedId = String.join(":", feed.getSlug(), feed.getUserID());
      params.add(new CustomQueryParameter("followers", feedId));
      params.add(new CustomQueryParameter("following", feedId));

      if (followerSlugs != null && followerSlugs.length > 0) {
        params.add(new CustomQueryParameter("followers_slugs", String.join(",", followerSlugs)));
      }
      if (followingSlugs != null && followingSlugs.length > 0) {
        params.add(new CustomQueryParameter("following_slugs", String.join(",", followingSlugs)));
      }

      return httpClient.execute(
          buildGet(url, key, token, params.toArray(new CustomQueryParameter[0])));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> updateActivityToTargets(
      Token token, FeedID feed, Activity activity, FeedID[] add, FeedID[] remove, FeedID[] replace)
      throws StreamException {
    checkNotNull(activity, "No activity to update");
    checkNotNull(activity.getForeignID(), "Activity is required to have foreign ID attribute");
    checkNotNull(activity.getTime(), "Activity is required to have time attribute");
    checkNotNull(add, "No targets to add");
    checkNotNull(remove, "No targets to remove");
    checkNotNull(replace, "No targets to set");
    boolean modification = replace.length == 0 && (add.length > 0 || remove.length > 0);
    boolean replacement = replace.length > 0 && add.length == 0 && remove.length == 0;
    checkArgument(
        modification || replacement,
        "Can't replace and modify activity to targets at the same time");

    final String[] addedTargets =
        J8Arrays.stream(add).map(id -> id.toString()).toArray(String[]::new);
    final String[] removedTargets =
        J8Arrays.stream(remove).map(id -> id.toString()).toArray(String[]::new);
    final String[] newTargets =
        J8Arrays.stream(replace).map(id -> id.toString()).toArray(String[]::new);

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public String foreign_id = activity.getForeignID();

                @JsonFormat(
                    shape = JsonFormat.Shape.STRING,
                    pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    lenient = OptBoolean.FALSE,
                    timezone = "UTC")
                public Date time = activity.getTime();

                public String[] added_targets = addedTargets;
                public String[] removed_targets = removedTargets;
                public String[] new_targets = newTargets;
              });
      final URL url = buildToTargetUpdateURL(baseURL, feed);
      return httpClient.execute(buildPost(url, key, token, payload));
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> getUser(Token token, String id, boolean withFollowCounts)
      throws StreamException {
    checkNotNull(id, "Missing user ID");
    checkArgument(!id.isEmpty(), "Missing user ID");

    try {
      final URL url = buildUsersURL(baseURL, id + '/');
      return httpClient.execute(
          buildGet(
              url,
              key,
              token,
              new CustomQueryParameter("with_follow_counts", Boolean.toString(withFollowCounts))));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> deleteUser(Token token, String id) throws StreamException {
    checkNotNull(id, "Missing user ID");
    checkArgument(!id.isEmpty(), "Missing user ID");

    try {
      final URL url = buildUsersURL(baseURL, id + '/');
      return httpClient.execute(buildDelete(url, key, token));
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> createUser(
      Token token, String userID, Data userData, boolean getOrCreate) throws StreamException {
    checkNotNull(userID, "Missing user ID");
    checkNotNull(userData, "Missing user data");
    checkArgument(!userID.isEmpty(), "Missing user ID");

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public String id = userID;
                public Map<String, Object> data = userData.getData();
              });
      final URL url = buildUsersURL(baseURL);
      return httpClient.execute(
          buildPost(
              url,
              key,
              token,
              payload,
              new CustomQueryParameter("get_or_create", Boolean.toString(getOrCreate))));
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Response> updateUser(Token token, String userID, Data userData)
      throws StreamException {
    checkNotNull(userID, "Missing user ID");
    checkNotNull(userData, "Missing user data");
    checkArgument(!userID.isEmpty(), "Missing user ID");

    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public Map<String, Object> data = userData.getData();
              });
      final URL url = buildUsersURL(baseURL, userID + '/');
      return httpClient.execute(buildPut(url, key, token, payload));
    } catch (JsonProcessingException | MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Object> deleteActivities(Token token, BatchDeleteActivitiesRequest request) throws StreamException {
    try {
            final URL url = deleteActivitiesURL(baseURL);
//      final URL url = deleteActivitiesURL(new URL("https://oregon-api.stream-io-api.com"));//$$ need to deploy proxy

      final byte[] payload = toJSON(request);
      io.getstream.core.http.Request httpRequest = buildPost(url, key, token, payload);
      return httpClient.execute(httpRequest).thenApply(response -> null);
    } catch (Exception e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Object> deleteReactions(Token token, BatchDeleteReactionsRequest request) throws StreamException {
    try {
      final URL url = deleteReactionsURL(baseURL);
//      final URL url = deleteReactionsURL(new URL("https://oregon-api.stream-io-api.com"));//$$ need to deploy proxy

      final byte[] payload = toJSON(request);
      io.getstream.core.http.Request httpRequest = buildPost(url, key, token, payload);

      //print the response

      return httpClient.execute(httpRequest).thenApply(response -> null);
    } catch (Exception e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<ExportIDsResponse> exportUserActivities(Token token, String userId) throws StreamException {
    if (userId == null || userId.isEmpty()) {
      throw new IllegalArgumentException("User ID can't be null or empty");
    }

    try {
            final URL url = buildExportIDsURL(baseURL, userId);
//      final URL url = buildExportIDsURL(new URL("https://oregon-api.stream-io-api.com"), userId);//$$ need to deploy proxy
      io.getstream.core.http.Request request = buildGet(url, key, token);
      return httpClient
              .execute(request)
              .thenApply(
                      response -> {
                        try {
                          return deserialize(response, ExportIDsResponse.class);
                        } catch (StreamException | IOException e) {
                          throw new CompletionException(e);
                        }
                      });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }
}
