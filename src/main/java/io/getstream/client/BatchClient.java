package io.getstream.client;

import static io.getstream.core.utils.Auth.*;

import com.google.common.collect.Iterables;
import io.getstream.core.KeepHistory;
import io.getstream.core.StreamBatch;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.*;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.utils.DefaultOptions;
import java.util.List;
import java8.util.J8Arrays;
import java8.util.concurrent.CompletableFuture;

public final class BatchClient {
  private final String secret;
  private final StreamBatch batch;

  BatchClient(String secret, StreamBatch batch) {
    this.secret = secret;
    this.batch = batch;
  }

  public CompletableFuture<Void> addToMany(Activity activity, FeedID... feeds)
      throws StreamException {
    final Token token = buildFeedToken(secret, TokenAction.WRITE);
    return batch.addToMany(token, activity, feeds);
  }

  public CompletableFuture<Void> followMany(int activityCopyLimit, FollowRelation... follows)
      throws StreamException {
    final Token token = buildFollowToken(secret, TokenAction.WRITE);
    return batch.followMany(token, activityCopyLimit, follows);
  }

  public CompletableFuture<Void> followMany(int activityCopyLimit, Iterable<FollowRelation> follows)
      throws StreamException {
    return followMany(activityCopyLimit, Iterables.toArray(follows, FollowRelation.class));
  }

  public CompletableFuture<Void> followMany(FollowRelation... follows) throws StreamException {
    return followMany(DefaultOptions.DEFAULT_ACTIVITY_COPY_LIMIT, follows);
  }

  public CompletableFuture<Void> followMany(Iterable<FollowRelation> follows)
      throws StreamException {
    return followMany(Iterables.toArray(follows, FollowRelation.class));
  }

  public CompletableFuture<Void> unfollowMany(FollowRelation... follows) throws StreamException {
    final Token token = buildFollowToken(secret, TokenAction.WRITE);
    final UnfollowOperation[] ops =
        J8Arrays.stream(follows)
            .map(follow -> new UnfollowOperation(follow, io.getstream.core.KeepHistory.YES))
            .toArray(UnfollowOperation[]::new);
    return batch.unfollowMany(token, ops);
  }

  public CompletableFuture<Void> unfollowMany(KeepHistory keepHistory, FollowRelation... follows)
      throws StreamException {
    final Token token = buildFollowToken(secret, TokenAction.WRITE);
    final UnfollowOperation[] ops =
        J8Arrays.stream(follows)
            .map(follow -> new UnfollowOperation(follow, keepHistory))
            .toArray(UnfollowOperation[]::new);
    return batch.unfollowMany(token, ops);
  }

  public CompletableFuture<Void> unfollowMany(UnfollowOperation... unfollows)
      throws StreamException {
    final Token token = buildFollowToken(secret, TokenAction.WRITE);
    return batch.unfollowMany(token, unfollows);
  }

  public CompletableFuture<List<Activity>> getActivitiesByID(Iterable<String> activityIDs)
      throws StreamException {
    return getActivitiesByID(Iterables.toArray(activityIDs, String.class));
  }

  public CompletableFuture<List<Activity>> getActivitiesByID(String... activityIDs)
      throws StreamException {
    final Token token = buildActivityToken(secret, TokenAction.READ);
    return batch.getActivitiesByID(token, activityIDs);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivitiesByID(
      Iterable<String> activityIDs) throws StreamException {
    return getEnrichedActivitiesByID(Iterables.toArray(activityIDs, String.class));
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivitiesByID(String... activityIDs)
      throws StreamException {
    return getEnrichedActivitiesByID(DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, activityIDs);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivitiesByID(
          EnrichmentFlags flags, String... activityIDs) throws StreamException {
    final Token token = buildActivityToken(secret, TokenAction.READ);
    return batch.getEnrichedActivitiesByID(token, flags, activityIDs);
  }

  public CompletableFuture<List<Activity>> getActivitiesByForeignID(
      Iterable<ForeignIDTimePair> activityIDTimePairs) throws StreamException {
    return getActivitiesByForeignID(
        Iterables.toArray(activityIDTimePairs, ForeignIDTimePair.class));
  }

  public CompletableFuture<List<Activity>> getActivitiesByForeignID(
      ForeignIDTimePair... activityIDTimePairs) throws StreamException {
    final Token token = buildActivityToken(secret, TokenAction.READ);
    return batch.getActivitiesByForeignID(token, activityIDTimePairs);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivitiesByForeignID(
      Iterable<ForeignIDTimePair> activityIDTimePairs) throws StreamException {
    return getEnrichedActivitiesByForeignID(
        Iterables.toArray(activityIDTimePairs, ForeignIDTimePair.class));
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivitiesByForeignID(
      ForeignIDTimePair... activityIDTimePairs) throws StreamException {
    final Token token = buildActivityToken(secret, TokenAction.READ);
    return batch.getEnrichedActivitiesByForeignID(token, activityIDTimePairs);
  }

  public CompletableFuture<Void> updateActivities(Iterable<Activity> activities)
      throws StreamException {
    return updateActivities(Iterables.toArray(activities, Activity.class));
  }

  public CompletableFuture<Void> updateActivities(Activity... activities) throws StreamException {
    final Token token = buildActivityToken(secret, TokenAction.WRITE);
    return batch.updateActivities(token, activities);
  }
}
