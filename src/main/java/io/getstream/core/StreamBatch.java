package io.getstream.core;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Request.buildGet;
import static io.getstream.core.utils.Request.buildPost;
import static io.getstream.core.utils.Routes.*;
import static io.getstream.core.utils.Serialization.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.models.*;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.options.RequestOption;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java8.util.J8Arrays;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public final class StreamBatch {
  private final String key;
  private final URL baseURL;
  private final HTTPClient httpClient;

  public StreamBatch(String key, URL baseURL, HTTPClient httpClient) {
    this.key = key;
    this.baseURL = baseURL;
    this.httpClient = httpClient;
  }

  public CompletableFuture<Void> addToMany(Token token, Activity activity, FeedID... feeds)
      throws StreamException {
    checkNotNull(activity, "Missing activity");
    checkNotNull(feeds, "No feeds to add to");
    checkArgument(feeds.length > 0, "No feeds to add to");

    // XXX: renaming the variable so we can unambiguously name payload field 'activity'
    Activity data = activity;
    String[] feedIDs = J8Arrays.stream(feeds).map(feed -> feed.toString()).toArray(String[]::new);
    try {
      final byte[] payload =
          toJSON(
              new Object() {
                public final Activity activity = data;
                public final String[] feeds = feedIDs;
              });
      final URL url = buildAddToManyURL(baseURL);
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
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

  public CompletableFuture<Void> followMany(
      Token token, int activityCopyLimit, FollowRelation... follows) throws StreamException {
    checkArgument(activityCopyLimit >= 0, "Activity copy limit must be non negative");
    checkNotNull(follows, "No feeds to follow");
    checkArgument(follows.length > 0, "No feeds to follow");

    try {
      final byte[] payload = toJSON(follows);
      final URL url = buildFollowManyURL(baseURL);
      return httpClient
          .execute(
              buildPost(
                  url,
                  key,
                  token,
                  payload,
                  new CustomQueryParameter(
                      "activity_copy_limit", Integer.toString(activityCopyLimit))))
          .thenApply(
              response -> {
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

  public CompletableFuture<Void> unfollowMany(Token token, UnfollowOperation... unfollows)
      throws StreamException {
    checkNotNull(unfollows, "No feeds to unfollow");
    checkArgument(unfollows.length > 0, "No feeds to unfollow");

    try {
      final byte[] payload = toJSON(unfollows);
      final URL url = buildUnfollowManyURL(baseURL);
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
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

  public CompletableFuture<List<Activity>> getActivitiesByID(Token token, String... activityIDs)
      throws StreamException {
    checkNotNull(activityIDs, "No activities to get");
    checkArgument(activityIDs.length > 0, "No activities to get");

    try {
      final URL url = buildActivitiesURL(baseURL);
      return httpClient
          .execute(
              buildGet(
                  url,
                  key,
                  token,
                  new CustomQueryParameter("ids", Joiner.on(",").join(activityIDs))))
          .thenApply(
              response -> {
                try {
                  return deserializeContainer(response, Activity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivitiesByID(
          Token token, EnrichmentFlags flags, String... activityIDs) throws StreamException {
    checkNotNull(activityIDs, "No activities to get");
    checkArgument(activityIDs.length > 0, "No activities to get");

    try {
      final URL url = buildEnrichedActivitiesURL(baseURL);
      return httpClient
          .execute(
              buildGet(
                  url,
                  key,
                  token,
                  flags,
                  new CustomQueryParameter("ids", Joiner.on(",").join(activityIDs))))
          .thenApply(
              response -> {
                try {
                  return deserializeContainer(response, EnrichedActivity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<List<Activity>> getActivitiesByForeignID(
      Token token, ForeignIDTimePair... activityIDTimePairs) throws StreamException {
    checkNotNull(activityIDTimePairs, "No activities to get");
    checkArgument(activityIDTimePairs.length > 0, "No activities to get");

    SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    timestampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    timestampFormat.setLenient(false);

    String[] foreignIDs =
        J8Arrays.stream(activityIDTimePairs)
            .map(pair -> pair.getForeignID())
            .toArray(String[]::new);
    String[] timestamps =
        J8Arrays.stream(activityIDTimePairs)
            .map(pair -> timestampFormat.format(pair.getTime()))
            .toArray(String[]::new);
    try {
      final URL url = buildActivitiesURL(baseURL);
      final RequestOption[] options =
          new RequestOption[] {
            new CustomQueryParameter("foreign_ids", Joiner.on(",").join(foreignIDs)),
            new CustomQueryParameter("timestamps", Joiner.on(",").join(timestamps))
          };
      return httpClient
          .execute(buildGet(url, key, token, options))
          .thenApply(
              response -> {
                try {
                  return deserializeContainer(response, Activity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivitiesByForeignID(
      Token token, ForeignIDTimePair... activityIDTimePairs) throws StreamException {
    checkNotNull(activityIDTimePairs, "No activities to get");
    checkArgument(activityIDTimePairs.length > 0, "No activities to get");

    SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    timestampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    timestampFormat.setLenient(false);

    String[] foreignIDs =
        J8Arrays.stream(activityIDTimePairs)
            .map(pair -> pair.getForeignID())
            .toArray(String[]::new);
    String[] timestamps =
        J8Arrays.stream(activityIDTimePairs)
            .map(pair -> timestampFormat.format(pair.getTime()))
            .toArray(String[]::new);
    try {
      final URL url = buildEnrichedActivitiesURL(baseURL);
      final RequestOption[] options =
          new RequestOption[] {
            new CustomQueryParameter("foreign_ids", Joiner.on(",").join(foreignIDs)),
            new CustomQueryParameter("timestamps", Joiner.on(",").join(timestamps))
          };
      return httpClient
          .execute(buildGet(url, key, token, options))
          .thenApply(
              response -> {
                try {
                  return deserializeContainer(response, EnrichedActivity.class);
                } catch (StreamException | IOException e) {
                  throw new CompletionException(e);
                }
              });
    } catch (MalformedURLException | URISyntaxException e) {
      throw new StreamException(e);
    }
  }

  public CompletableFuture<Void> updateActivities(Token token, Activity... activities)
      throws StreamException {
    checkNotNull(activities, "No activities to update");
    checkArgument(activities.length > 0, "No activities to update");

    try {
      // XXX: renaming the variable so we can unambiguously name payload field 'activities'
      Activity[] data = activities;
      final byte[] payload =
          toJSON(
              new Object() {
                public final Activity[] activities = data;
              });
      final URL url = buildActivitiesURL(baseURL);
      return httpClient
          .execute(buildPost(url, key, token, payload))
          .thenApply(
              response -> {
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
}
