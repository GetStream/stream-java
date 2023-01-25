package io.getstream.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.*;
import static io.getstream.core.utils.Serialization.deserializeContainer;

import com.google.common.collect.Iterables;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.*;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.options.Limit;
import io.getstream.core.options.Offset;
import io.getstream.core.options.RequestOption;
import io.getstream.core.utils.DefaultOptions;
import io.getstream.core.utils.Streams;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java8.util.J8Arrays;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public class Feed {
  private final Client client;
  private final FeedID id;

  Feed(Client client, FeedID id) {
    checkNotNull(client, "Can't create feed w/o a client");
    checkNotNull(id, "Can't create feed w/o an ID");
    checkArgument(!id.getSlug().isEmpty(), "Feed slug can't be empty");

    this.client = client;
    this.id = id;
  }

  protected final Client getClient() {
    return client;
  }

  public final FeedID getID() {
    return id;
  }

  public final String getSlug() {
    return id.getSlug();
  }

  public final String getUserID() {
    return id.getUserID();
  }

  public final CompletableFuture<Activity> addActivity(Activity activity) throws StreamException {
    return getClient()
        .addActivity(id, activity)
        .thenApply(
            response -> {
              try {
                return deserialize(response, Activity.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final <T> CompletableFuture<T> addCustomActivity(T activity) throws StreamException {
    return getClient()
        .addActivity(id, Activity.builder().fromCustomActivity(activity).build())
        .thenApply(
            response -> {
              try {
                return deserialize(response, (Class<T>) activity.getClass());
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<List<Activity>> addActivities(Iterable<Activity> activities)
      throws StreamException {
    return addActivities(Iterables.toArray(activities, Activity.class));
  }

  public final <T> CompletableFuture<List<T>> addCustomActivities(Iterable<T> activities)
      throws StreamException {
    final Activity[] custom =
        Streams.stream(activities)
            .map(activity -> Activity.builder().fromCustomActivity(activity).build())
            .toArray(Activity[]::new);
    return getClient()
        .addActivities(id, custom)
        .thenApply(
            response -> {
              try {
                Class<T> element =
                    (Class<T>)
                        ((ParameterizedType) getClass().getGenericSuperclass())
                            .getActualTypeArguments()[0];
                return deserializeContainer(response, "activities", element);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<List<Activity>> addActivities(Activity... activities)
      throws StreamException {
    return getClient()
        .addActivities(id, activities)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, "activities", Activity.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final <T> CompletableFuture<List<T>> addCustomActivities(T... activities)
      throws StreamException {
    final Activity[] custom =
        J8Arrays.stream(activities)
            .map(activity -> Activity.builder().fromCustomActivity(activity).build())
            .toArray(Activity[]::new);
    return getClient()
        .addActivities(id, custom)
        .thenApply(
            response -> {
              try {
                Class<T> element = (Class<T>) activities.getClass().getComponentType();
                return deserializeContainer(response, "activities", element);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<Void> removeActivityByID(String id) throws StreamException {
    return client
        .removeActivityByID(this.id, id)
        .thenApply(
            response -> {
              try {
                return deserializeError(response);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<Void> removeActivityByForeignID(String foreignID)
      throws StreamException {
    return client
        .removeActivityByForeignID(id, foreignID)
        .thenApply(
            response -> {
              try {
                return deserializeError(response);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<Void> follow(FlatFeed feed) throws StreamException {
    return follow(feed, DefaultOptions.DEFAULT_ACTIVITY_COPY_LIMIT);
  }

  public final CompletableFuture<Void> follow(FlatFeed feed, int activityCopyLimit)
      throws StreamException {
    checkArgument(
        activityCopyLimit <= DefaultOptions.MAX_ACTIVITY_COPY_LIMIT,
        String.format(
            "Activity copy limit should be less then %d", DefaultOptions.MAX_ACTIVITY_COPY_LIMIT));

    return client
        .follow(id, feed.getID(), activityCopyLimit)
        .thenApply(
            response -> {
              try {
                return deserializeError(response);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(Iterable<FeedID> feedIDs)
      throws StreamException {
    return getFollowers(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(FeedID... feedIDs)
      throws StreamException {
    return getFollowers(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, feedIDs);
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(
      Limit limit, Iterable<FeedID> feedIDs) throws StreamException {
    return getFollowers(
        limit, DefaultOptions.DEFAULT_OFFSET, Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(Limit limit, FeedID... feedIDs)
      throws StreamException {
    return getFollowers(limit, DefaultOptions.DEFAULT_OFFSET, feedIDs);
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(
      Offset offset, Iterable<FeedID> feedIDs) throws StreamException {
    return getFollowers(
        DefaultOptions.DEFAULT_LIMIT, offset, Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(
      Offset offset, FeedID... feedIDs) throws StreamException {
    return getFollowers(DefaultOptions.DEFAULT_LIMIT, offset, feedIDs);
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(
      Limit limit, Offset offset, Iterable<FeedID> feedIDs) throws StreamException {
    return getFollowers(limit, offset, Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowers(
      Limit limit, Offset offset, FeedID... feeds) throws StreamException {
    checkNotNull(feeds, "No feed ids to filter on");

    final String[] feedIDs = J8Arrays.stream(feeds).map(id -> id.toString()).toArray(String[]::new);
    final RequestOption[] options =
        feedIDs.length == 0
            ? new RequestOption[] {limit, offset}
            : new RequestOption[] {
              limit, offset, new CustomQueryParameter("filter", String.join(",", feedIDs))
            };
    return client
        .getFollowers(id, options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, FollowRelation.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(Iterable<FeedID> feedIDs)
      throws StreamException {
    return getFollowed(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(FeedID... feedIDs)
      throws StreamException {
    return getFollowed(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, feedIDs);
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(
      Offset offset, Iterable<FeedID> feedIDs) throws StreamException {
    return getFollowed(
        DefaultOptions.DEFAULT_LIMIT, offset, Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(Offset offset, FeedID... feedIDs)
      throws StreamException {
    return getFollowed(DefaultOptions.DEFAULT_LIMIT, offset, feedIDs);
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(
      Limit limit, Iterable<FeedID> feedIDs) throws StreamException {
    return getFollowed(
        limit, DefaultOptions.DEFAULT_OFFSET, Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(Limit limit, FeedID... feedIDs)
      throws StreamException {
    return getFollowed(limit, DefaultOptions.DEFAULT_OFFSET, feedIDs);
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(
      Limit limit, Offset offset, Iterable<FeedID> feedIDs) throws StreamException {
    return getFollowed(limit, offset, Iterables.toArray(feedIDs, FeedID.class));
  }

  public final CompletableFuture<List<FollowRelation>> getFollowed(
      Limit limit, Offset offset, FeedID... feeds) throws StreamException {
    checkNotNull(feeds, "No feed ids to filter on");

    final String[] feedIDs = J8Arrays.stream(feeds).map(id -> id.toString()).toArray(String[]::new);
    final RequestOption[] options =
        feedIDs.length == 0
            ? new RequestOption[] {limit, offset}
            : new RequestOption[] {
              limit, offset, new CustomQueryParameter("filter", String.join(",", feedIDs))
            };
    return client
        .getFollowed(id, options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, FollowRelation.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<Void> unfollow(FlatFeed feed) throws StreamException {
    return unfollow(feed, io.getstream.core.KeepHistory.NO);
  }

  public final CompletableFuture<Void> unfollow(
      FlatFeed feed, io.getstream.core.KeepHistory keepHistory) throws StreamException {
    return client
        .unfollow(id, feed.getID(), new io.getstream.core.options.KeepHistory(keepHistory))
        .thenApply(
            response -> {
              try {
                return deserializeError(response);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<FollowStats> getFollowStats(
      Iterable<String> followerSlugs, Iterable<String> followingSlugs) throws StreamException {
    return client
        .getFollowStats(
            id,
            Iterables.toArray(followerSlugs, String.class),
            Iterables.toArray(followingSlugs, String.class))
        .thenApply(
            response -> {
              try {
                return deserializeContainerSingleItem(response, FollowStats.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<Void> updateActivityToTargets(
      Activity activity, Iterable<FeedID> add, Iterable<FeedID> remove) throws StreamException {
    return updateActivityToTargets(
        activity, Iterables.toArray(add, FeedID.class), Iterables.toArray(remove, FeedID.class));
  }

  public final CompletableFuture<Void> updateActivityToTargets(
      Activity activity, FeedID[] add, FeedID[] remove) throws StreamException {
    return client
        .updateActivityToTargets(id, activity, add, remove, new FeedID[0])
        .thenApply(
            response -> {
              try {
                return deserializeError(response);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public final CompletableFuture<Void> replaceActivityToTargets(
      Activity activity, Iterable<FeedID> newTargets) throws StreamException {
    return replaceActivityToTargets(activity, Iterables.toArray(newTargets, FeedID.class));
  }

  public final CompletableFuture<Void> replaceActivityToTargets(
      Activity activity, FeedID... newTargets) throws StreamException {
    return client
        .updateActivityToTargets(id, activity, new FeedID[0], new FeedID[0], newTargets)
        .thenApply(
            response -> {
              try {
                return deserializeError(response);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }
}
